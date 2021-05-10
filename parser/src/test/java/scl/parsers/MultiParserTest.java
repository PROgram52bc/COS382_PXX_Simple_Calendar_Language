package scl.parsers;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultiParserTest {
    class NullParser implements StringParser<Integer> {
        public Integer parse(String input) {
            return null;
        }
    }
    class IntegerParser implements StringParser<Integer> {
        public Integer parse(String input) {
            return Integer.valueOf(input);
        }
    }
    class FortyTwoParser implements StringParser<Integer> {
        public Integer parse(String input) {
            return Integer.valueOf(42);
        }
    }
    class CascadeParser extends MultiParser<Integer> {
        public CascadeParser() {
            addParser(new NullParser());
            addParser(new FortyTwoParser());
        }
    }
    @Test
    public void testCascadeParser() {
        CascadeParser cp = new CascadeParser();
        assertEquals(Integer.valueOf(42), cp.parse(""));
    }
    @Test
    public void testTemporaryMultiParser() {
        MultiParser<Integer> mp = new MultiParser<>();
        mp.addParser(new NullParser())
            .addParser(new IntegerParser())
            .addParser(new CascadeParser());
        assertEquals(Integer.valueOf(17), mp.parse("17"));
    }

}
