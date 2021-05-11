package scl;
import scl.util.ProxyMap;

import org.junit.Test;
import static org.junit.Assert.*;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class PropReaderTest {
    private PropReader getReader(String input) {
        SCLLexer lexer = new SCLLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SCLParser parser = new SCLParser(tokens);
        parser.setBuildParseTree(true); // tell ANTLR to build a parse tree
        ParseTree tree = parser.file();
        ParseTreeWalker walker = new ParseTreeWalker();
        PropReader reader = new PropReader();
        walker.walk(reader, tree);
        return reader;
    }

    @Test
    public void testSingleLineField() {
        String input
            = "event Chorale\n"
            + "from: <May 1, 2021 7:00PM>\n"
            + "to: <May 1, 2021 10:00PM>\n"
            + "\n";
        PropReader reader = getReader(input);
        ProxyMap choraleEvent = reader.getEvent("Chorale");
        assertNotNull("the event should be properly registered",
                choraleEvent);
        assertEquals("the 'from' attribute should have correct value",
                choraleEvent.get("from"), "May 1, 2021 7:00PM");
    }
    @Test
    public void testMultiLineField() {
        String description =
            "Dr. Spencer hates being people being late\n" +
            "So don't be late!";
        String input
            = "event Chorale\n"
            + "description: <" + description + ">\n"
            + "from: <May 1, 2021 7:00PM>\n"
            + "to: <May 1, 2021 10:00PM>\n"
            + "\n";
        PropReader reader = getReader(input);
        ProxyMap choraleEvent = reader.getEvent("Chorale");
        assertEquals(
                "The multi-line description should have the correct value",
                choraleEvent.get("description"),
                description);
    }
    @Test
    public void testMultipleEvents() {
        String input
            = "event Chorale\n"
            + "description: <Dr. Spencer hates being people being late\n"
            + "So don't be late!>\n"
            + "from: <May 1, 2021 7:00PM>\n"
            + "to: <May 1, 2021 10:00PM>\n"
            + "\n"
            + "event COS382\n"
            + "description: <The language structures class>\n";
        PropReader reader = getReader(input);
        assertNotNull(
                "Chorale should be registered",
                reader.getEvent("Chorale"));
        assertNotNull(
                "COS382 should be registered",
                reader.getEvent("COS382"));
        assertEquals(
                "COS382 should have the proper description",
                reader.getEvent("COS382").get("description"),
                "The language structures class");
    }
}
