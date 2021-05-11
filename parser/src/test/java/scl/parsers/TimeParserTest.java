package scl.parsers;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalTime;

public class TimeParserTest {
    private TimeParser parser;

    @Before
    public void initParser() {
        parser = new TimeParser();
    }

    @Test
	public void testCommon() {
        String input = "11:55 PM";
        LocalTime dt = parser.parse(input);
        assertNotNull("parser returned null", dt);
        assertEquals("hour should be correct", 23, dt.getHour());
        assertEquals("minute should be correct", 55, dt.getMinute());
    }

    @Test
	public void testMilitary() {
        String input = "23:55";
        LocalTime dt = parser.parse(input);
        assertNotNull("parser returned null", dt);
        assertEquals("hour should be correct", 23, dt.getHour());
        assertEquals("minute should be correct", 55, dt.getMinute());
    }

    @Test
	public void testInvalid() {
        String input = "invalid date format";
        assertNull("parser should return null on invalid input", parser.parse(input));
    }
}
