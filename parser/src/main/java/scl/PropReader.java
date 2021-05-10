package scl;
import scl.util.*;

import java.util.*;
import java.lang.NullPointerException;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import biweekly.property.*;
import biweekly.component.*;
import biweekly.util.*;
import biweekly.io.*;
import biweekly.ICalendar;

/**
 * The main listener class that reads properties in an event
 * */
public class PropReader extends SCLBaseListener {
    /** a map to store temporary attributes for the current event */
    private final HashMap<String, ProxyMap> events;
    // TODO: how to store attributes like event type? Using a leading underscore? <2021-05-08, David Deng> //
    private ProxyMap global;
    private ProxyMap currentEvent;

    private ProxyMap getEvent(String eventName) {
        return events.getOrDefault(eventName, null);
    }

    public PropReader() {
        events = new HashMap<>();
        global = new ProxyMap();
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
        // // event type currently not used
        // currentEventType = ctx.eventtype().getText();
        // if (currentEventType.isEmpty()) {
        //     currentEventType = "general";
        // }
        Debugger.log("event type: " + ctx.eventtype().getText());

        String name = ctx.ID().getText();
        Debugger.log("Event name: " + name);
        currentEvent.put("name", name);
    }
    @Override
    public void enterEventattr(SCLParser.EventattrContext ctx) {
        String attributeName = ctx.ID().getText();
        Debugger.log("Attribute name: " + attributeName);
        // remove quotes
        // TODO: support more types of value? add optional quote requirement? <2021-05-08, David Deng> //
        String attributeValue = ctx.value().getText()
            .replaceAll("^\"", "")
            .replaceAll("\"$", "");
        currentEvent.put(attributeName, attributeValue);
    }
    @Override
    public void enterStringvalue(SCLParser.StringvalueContext ctx) {
        // Debugger.log("Entering string value: " + ctx.getText());
        // AttributesHandler handler;
        // try {
        //     handler = handlers.get(currrentAttributeName);
        // } catch (NullPointerException e) {
        //     Debugger.log("currrentAttributeName: " + currrentAttributeName);
        //     Debugger.log("Null pointer exception: " + e.toString());
        //     return;
        // }
        // handler.parseAttribute(currrentAttributeName, value, events.get(currentEvent.get("name")));
    }
    @Override
    public void exitEvent(SCLParser.EventContext ctx) {
        // end of an event
        Debugger.log("Exiting Event: " + currentEvent.get("name"));
        Debugger.log(events.get(currentEvent.get("name")));
        events.put(currentEvent.get("name"), new ProxyMap(currentEvent));
        currentEvent.clear();
    }
}
