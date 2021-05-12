package scl;
import scl.util.*;

import java.util.*;
import biweekly.io.*;

/**
 * The main listener class that reads properties in an event
 * */
public class PropReader extends SCLBaseListener {
    // a map to store temporary attributes for all events read
    private final HashMap<String, ProxyMap> events;

    // TODO: Utilize the global map to store timezone information <2021-05-11, David Deng> //
    // private ProxyMap global;

    // any key starting with an underscore '_' is reserved
    private ProxyMap currentEvent;

    /**
     * get a copy of the specified event.
     * @param eventName the name of the event to be retrieved.
     * @return a {@link ProxyMap} representing all attributes of the event. Null if the event doesn't exist.
     **/
    public ProxyMap getEvent(String eventName) {
        ProxyMap event = events.getOrDefault(eventName, null);
        return event == null ? null : new ProxyMap(event);
    }

    public PropReader() {
        events = new HashMap<>();
        // global = new ProxyMap();
        currentEvent = new ProxyMap();
    }

    /**
     * Get a {@link TimezoneAssignment} object based on a timezone id
     * @param timezoneId an id for the {@link TimeZone} class
     * @return a {@link TimezoneAssignment} object, null if id is invalid
     * */
    public TimezoneAssignment getTimezoneAssignment(String timezoneId) {
        String[] ids = TimeZone.getAvailableIDs();
        if (!Arrays.asList(ids).contains(timezoneId)) {
            Debugger.log(1, "Timezone id " + timezoneId + " not available. Available ids:");
            for (String id : ids) {
                Debugger.log(1, "- " + id);
            }
            return null;
        }
        TimeZone tz = TimeZone.getTimeZone(timezoneId);
        TimezoneAssignment tza = TimezoneAssignment.download(tz, false); // don't need to suit for MS outlook
        return tza;
    }
    /**
     * Get an iterable of the events parsed by the reader, each represented by a {@link ProxyMap}
     *
     * @return an iterable containing a copy of all the {@link ProxyMap} produced by the reader.
     **/
    public Iterable<ProxyMap> getEventProxyMaps() {
        List<ProxyMap> copy = new ArrayList<>();
        for (ProxyMap pm: events.values()) {
            copy.add(new ProxyMap(pm));
        }
        return copy;
    }

    @Override
    public void enterEventhead(SCLParser.EventheadContext ctx) {
        // event type currently not used
        String eventType = ctx.eventtype().getText();
        if (eventType.isEmpty()) {
            eventType = "full"; // full, partial
        }
        String eventName = ctx.ID().getText();
        Debugger.log(2, "eventType: " + eventType);
        Debugger.log(2, "eventName: " + eventName);
        currentEvent.put("_type", eventType); // hidden attribute
        currentEvent.put("_name", eventName);
        currentEvent.put("summary", eventName);
    }
    @Override
    public void enterEventattr(SCLParser.EventattrContext ctx) {
        String attributeName = ctx.ID().getText();
        // remove surrounding angle brackets from the value
        String attributeValue = ctx.value().getText()
            .replaceAll("^<", "")
            .replaceAll(">$", "");
        currentEvent.put(attributeName, attributeValue);
        Debugger.log(3, "attributeName: " + attributeName);
        Debugger.log(3, "attributeValue: " + attributeValue);
    }
    @Override
    public void exitEvent(SCLParser.EventContext ctx) {
        // end of an event
        String eventName = currentEvent.get("_name");
        Debugger.log(2, "Exiting event: " + eventName);
        events.put(eventName, new ProxyMap(currentEvent));
        currentEvent.clear();
    }
}
