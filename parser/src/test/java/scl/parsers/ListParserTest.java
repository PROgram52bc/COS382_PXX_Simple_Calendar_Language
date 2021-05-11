package scl.parsers;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ListParserTest {
    @Test
    public void testTrim() {
        ListParser parser = new ListParser(",", true);
        List<String> result;
        result = parser.parse("  one, two ,  three");
        assertEquals("there should be three items in the list", 3, result.size());
        assertEquals("the first item should be 'one'", "one", result.get(0));
        assertEquals("the first item should be 'two'", "two", result.get(1));
        assertEquals("the first item should be 'three'", "three", result.get(2));
    }
}
