package scl.parsers;
import scl.util.Debugger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parser that converts a string into a {@link LocalTime} object
 **/
public class TimeParser extends MultiParser<LocalTime> {
	public TimeParser() {
		addParser(new Common());
		addParser(new Military());
		// addParser(...)
	}

	/**
	 * A base class for parsing complete time strings.
	 *
	 * Multiple instances of classes derived from this class should be added
	 * to a {@link MultiParser} to achieve fallback effect.
	 **/
	public static class Base implements StringParser<LocalTime> {
		protected String pattern;

		/**
		 * Helper function to parse a string with a specified {@link Formatter} object
		 **/
		protected LocalTime parseWithFormatter(String input, DateTimeFormatter formatter) {
			try {
				return LocalTime.parse(input, formatter);
			} catch (DateTimeParseException e) {
				Debugger.log(2, "TimeParser with pattern [" + pattern + "] failed: " + e);
				return null;
			}
		}

		/**
		 * Constructor.
		 * @param pattern the full time pattern string to be parsed
		 **/
		public Base(String pattern) {
			this.pattern = pattern;
		}

		@Override
		public LocalTime parse(String input) {
			return parseWithFormatter(input, DateTimeFormatter.ofPattern(pattern));
		}
	}

	/**
	 * Parser for a time string.
	 *
	 * e.g. "11:55 AM"
	 **/
	public static class Common extends Base {
		public Common() {
			super("hh:mm a");
		}
	}
	/**
	 * Parser for a time string in the military style.
	 *
	 * e.g. "22:00"
	 **/
	public static class Military extends Base {
		public Military() {
			super("HH:mm");
		}
	}
}
