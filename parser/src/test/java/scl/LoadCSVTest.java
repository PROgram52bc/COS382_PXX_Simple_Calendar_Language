package scl;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class LoadCSVTest {
    @Test 
    public void testLoadSimpleCSV() {
        String testInput = "field1, field2, field3\nr1val1,r1val2,r1val3\nr2val1,r2val2,r2val3";

        CSVLexer lexer = new CSVLexer(CharStreams.fromString(testInput));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CSVParser parser = new CSVParser(tokens);
        parser.setBuildParseTree(true); // tell ANTLR to build a parse tree
        ParseTree tree = parser.file();

        ParseTreeWalker walker = new ParseTreeWalker();
        LoadCSV.Loader loader = new LoadCSV.Loader();
        walker.walk(loader, tree);

        assertEquals("Should have parsed 2 content rows", loader.rows.size(), 2);
        assertEquals("first row first column should have the correct content",
                loader.rows.get(0).get("field1"), "r1val1");
    }
}
