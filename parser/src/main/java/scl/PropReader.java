package scl;

import java.util.*;
import java.lang.NullPointerException;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import biweekly.property.*;
import biweekly.component.*;
import biweekly.util.*;
import biweekly.io.*;
import biweekly.ICalendar;

public class PropReader extends SCLBaseListener {

    ///////////////////////////
    //  initialize calendar  //
    ///////////////////////////
    private final Hashtable<String, AttributesHandler> handlers;
    private final Hashtable<String, VEvent> events;
    private String currentEventName;
    private String currentEventType;
    private String currrentAttributeName;

    public PropReader() {
        events = new Hashtable<>();
        handlers = new Hashtable<>();
        handlers.put("from", new GeneralAttributesHandler());
        handlers.put("to", new GeneralAttributesHandler());
    }
    public VEvent getEvent(String eventName) {
        return events.get(eventName);
    }
    public TimezoneAssignment getTimezone(String timezoneId) {
        String[] ids = TimeZone.getAvailableIDs();
        if (!Arrays.asList(ids).contains(timezoneId)) {
            System.out.println("Timezone id " + timezoneId + " not available. Available ids:");
            for (String id : ids) {
                System.out.println("- " + id);
            }
            return null;
        }
        TimeZone tz = TimeZone.getTimeZone(timezoneId);
        TimezoneAssignment tza = TimezoneAssignment.download(tz, false); // don't need to suit for MS outlook
        return tza;
    }
    public ICalendar getCalendar() {
        ICalendar ical = new ICalendar();

        // set timezone
        // TODO: parameterize the timezone information
        // TODO: add a timezone
        TimezoneAssignment timezone = getTimezone("America/New_York");
        TimezoneInfo tzinfo = new TimezoneInfo();
        tzinfo.setDefaultTimezone(timezone);
        ical.setTimezoneInfo(tzinfo);

        // add all events to the calendar
        for (VEvent e : events.values()) {
            ical.addEvent(e);
        }
        return ical;
    }
    @Override
    public void enterEvent(SCLParser.EventContext ctx) {
        System.out.println("Entering event. Attribute count: " + ((ctx.getChildCount() - 1)/2));
    }
    @Override
    public void enterEventhead(SCLParser.EventheadContext ctx) {
        System.out.println("Entering Eventhead. Event name: " + ctx.ID().getText());
        System.out.println("event type: " + ctx.eventtype().getText());
        currentEventName = ctx.ID().getText();
        currentEventType = ctx.eventtype().getText();
        if (currentEventType.isEmpty()) {
            currentEventType = "general";
        }
        events.put(currentEventName, new VEvent());
        events.get(currentEventName).setSummary(currentEventName);
    }
    @Override
    public void enterEventattr(SCLParser.EventattrContext ctx) {
        currrentAttributeName = ctx.ID().getText();
        System.out.print("Entering EventAttribute: ");
        System.out.println(currrentAttributeName);
    }
    @Override
    public void enterStringvalue(SCLParser.StringvalueContext ctx) {
        System.out.println("Entering string value: " + ctx.getText());
        String value = ctx.getText().replaceAll("^\"", "").replaceAll("\"$", "");
        AttributesHandler handler;
        try {
            handler = handlers.get(currrentAttributeName);
        } catch (NullPointerException e) {
            System.out.println("currrentAttributeName: " + currrentAttributeName);
            System.out.println("Null pointer exception: " + e.toString());
            return;
        }
        handler.parseAttribute(currrentAttributeName, value, events.get(currentEventName));
    }
    @Override
    public void enterListvalue(SCLParser.ListvalueContext ctx) {
        System.out.println("Entering list value (unhandled): " + ctx.getText());
        for (TerminalNode s : ctx.list().STRING()) {
            System.out.println("-> " + s.getText());
        }
    }
    @Override
    public void exitEventattr(SCLParser.EventattrContext ctx) {
        System.out.print("Exiting EventAttribute: ");
        System.out.println(ctx.getText());
    }
    @Override
    public void exitEvent(SCLParser.EventContext ctx) {
        // end of an event
        System.out.println("Exiting Event: " + currentEventName);
        System.out.println(events.get(currentEventName));
    }
}
