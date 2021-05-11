package scl.parsers;
import scl.util.Debugger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parser that converts a string into a {@link LocalDateTime} object
 **/
public class DateTimeParser extends MultiParser<LocalDateTime> {
	public DateTimeParser() {
		addParser(new Common());
		addParser(new Military());
		// addParser(...)
	}

	/**
	 * A base class for parsing complete datetime strings.
	 *
	 * Multiple instances of classes derived from this class should be added
	 * to a {@link MultiParser} to achieve fallback effect.
	 **/
	public static class Base implements Parser<LocalDateTime> {
		protected String pattern;

		/**
		 * Helper function to parse a string with a specified {@link Formatter} object
		 **/
		protected LocalDateTime parseWithFormatter(String input, DateTimeFormatter formatter) {
			try {
				return LocalDateTime.parse(input, formatter);
			} catch (DateTimeParseException e) {
				Debugger.log(2, "DateTimeParser with pattern [" + pattern + "] failed: " + e);
				return null;
			}
		}

		/**
		 * Constructor.
		 * @param pattern the full datetime pattern string to be parsed
		 **/
		public Base(String pattern) {
			this.pattern = pattern;
		}

		@Override
		public LocalDateTime parse(String input) {
			return parseWithFormatter(input, DateTimeFormatter.ofPattern(pattern));
		}
	}
	/**
	 * Parser for a datetime string.
	 *
	 * e.g. "Jul 4, 2021 11:55 AM"
	 **/
	public static class Common extends Base {
		public Common() {
			super("MMM d, yyyy hh:mm a");
		}
	}
	/**
	 * Parser for a datetime string in the military style.
	 *
	 * e.g. "2021-7-5 22:00"
	 **/
	public static class Military extends Base {
		public Military() {
			super("yyyy-M-d HH:mm");
		}
	}
}
