package scl;
import scl.util.*;
import scl.handlers.*;

import java.util.*;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import biweekly.ICalendar;
import biweekly.component.VEvent;

public class App {
    public static void main(String[] args) {

        SCLLexer lexer;
        try {
            // lexer = new SCLLexer(CharStreams.fromString(testInput));
            // TODO: parameterize input file name <2021-05-08, David Deng> //
            lexer = new SCLLexer(CharStreams.fromFileName("test.scl"));
        } catch (IOException e) {
            Debugger.log("Failed to open file. " + e.toString());
            return;
        }
        // TODO: check for parse error <2021-05-08, David Deng> //
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
        ICalendar ical = new ICalendar();
        EventFactory eventFactory = new EventFactory();
        eventFactory.registerHandler(new FromHandler());
        eventsProxyMaps.forEach(pm -> {
            VEvent event = eventFactory.makeEvent(pm);
            ical.addEvent(event);
        });
        Debugger.log(2, "Calendar content below ------------");
        Debugger.log(2, ical.toString());
        try {
            // TODO: parameterize output file name <2021-05-08, David Deng> //
            FileOutputStream out = new FileOutputStream("test.ics");
            ical.write(out);
        } catch (IOException e) {
            Debugger.log("Failed to write to file. " + e.toString());
        }
    }
}
