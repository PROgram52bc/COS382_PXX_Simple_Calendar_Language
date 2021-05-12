package scl;
// TODO: change the package name <2021-05-10, David Deng> //
// package com.gmail.daviddenghaotian.scl;
import scl.util.*;
import scl.handlers.*;

import java.io.FileOutputStream;
import java.io.IOException;

import java.time.ZoneId;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import biweekly.ICalendar;
import biweekly.ICalVersion;
import biweekly.ValidationWarnings;
import biweekly.component.VEvent;
import biweekly.io.TimezoneInfo;
import biweekly.io.TimezoneAssignment;

public class App {
    public static void main(String[] args) {
        // initialize ANTLR
        SCLLexer lexer;
        try {
            // TODO: parameterize input file name <2021-05-08, David Deng> //
            lexer = new SCLLexer(CharStreams.fromFileName("test.scl"));
        } catch (IOException e) {
            Debugger.log("Failed to open file. " + e.toString());
            return;
        }
        // TODO: check for parse error in ANTLR <2021-05-08, David Deng> //
        // Investigate how ANTLR handles parse error.
        // Is there a programmatic way to retrieve parse errors?
        // Disable continuing upon error.
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SCLParser parser = new SCLParser(tokens);
        parser.setBuildParseTree(true); // tell ANTLR to build a parse tree
        ParseTree tree = parser.file();

        ParseTreeWalker walker = new ParseTreeWalker();
        PropReader reader = new PropReader();
        walker.walk(reader, tree);
        Iterable<ProxyMap> eventsProxyMaps = reader.getEventProxyMaps();

        // Create EventFactory and register handlers
        EventFactory eventFactory = new EventFactory();
        eventFactory.registerHandler(new FromHandler());
        eventFactory.registerHandler(new ToHandler());
        eventFactory.registerHandler(new EveryHandler());
        eventFactory.registerHandler(new SummaryHandler());
        eventFactory.registerHandler(new DescriptionHandler());
        eventFactory.registerHandler(new LocationHandler());

        // parse the proxyMaps
        ICalendar ical = new ICalendar();
        eventsProxyMaps.forEach(pm -> {
            VEvent event = eventFactory.makeEvent(pm);
            ical.addEvent(event);
        });

        // add timezone
        // TODO: allow overriding default output timezone? <2021-05-11, David Deng> //
        String defaultZoneId = ZoneId.systemDefault().getId();
        TimezoneAssignment timezone = reader.getTimezoneAssignment(defaultZoneId);
        TimezoneInfo tzinfo = new TimezoneInfo();
        tzinfo.setDefaultTimezone(timezone);
        ical.setTimezoneInfo(tzinfo);

        // validate the calendar object
        ValidationWarnings warnings = ical.validate(ICalVersion.V2_0);
        if (!warnings.isEmpty()) {
            Debugger.log(1, "Validation warnings: " + warnings);
        }

        Debugger.log(2, "Calendar content below ------------");
        Debugger.log(2, ical.toString());
        Debugger.log(2, ical.write());

        // write to output file
        try {
            // TODO: parameterize output file name <2021-05-08, David Deng> //
            FileOutputStream out = new FileOutputStream("test.ics");
            ical.write(out);
        } catch (IOException e) {
            Debugger.log("Failed to write to file. " + e.toString());
        }
    }
}
