package scl.parsers;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

public class DateTimeParserTest {
    private DateTimeParser parser;

    @Before
    public void initParser() {
        parser = new DateTimeParser();
    }

    @Test
	public void testCommon() {
        String input = "Jul 4, 2021 11:55 PM";
        LocalDateTime dt = parser.parse(input);
        assertNotNull("parser returned null", dt);
        assertEquals("year should be correct", 2021, dt.getYear());
        assertEquals("month should be correct", 7, dt.getMonthValue());
        assertEquals("date should be correct", 4, dt.getDayOfMonth());
        assertEquals("hour should be correct", 23, dt.getHour());
        assertEquals("minute should be correct", 55, dt.getMinute());
    }

    @Test
	public void testMilitary() {
        String input = "2021-7-4 23:55";
        LocalDateTime dt = parser.parse(input);
        assertNotNull("parser returned null", dt);
        assertEquals("year should be correct", 2021, dt.getYear());
        assertEquals("month should be correct", 7, dt.getMonthValue());
        assertEquals("date should be correct", 4, dt.getDayOfMonth());
        assertEquals("hour should be correct", 23, dt.getHour());
        assertEquals("minute should be correct", 55, dt.getMinute());
    }

    @Test
	public void testInvalid() {
        String input = "invalid date format";
        assertNull("parser should return null on invalid input", parser.parse(input));
    }
}
