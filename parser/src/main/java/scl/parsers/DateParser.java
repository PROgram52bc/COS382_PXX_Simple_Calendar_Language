package scl.parsers;
import scl.util.Debugger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parser that converts a string into a {@link LocalDate} object
 **/
public class DateParser extends MultiParser<LocalDate> {
	public DateParser() {
		addParser(new Common());
		addParser(new Military());
		// addParser(...)
	}

	/**
	 * A base class for parsing complete date strings.
	 *
	 * Multiple instances of classes derived from this class should be added
	 * to a {@link MultiParser} to achieve fallback effect.
	 **/
	public static class Base implements Parser<LocalDate> {
		protected String pattern;

		/**
		 * Helper function to parse a string with a specified {@link Formatter} object
		 **/
		protected LocalDate parseWithFormatter(String input, DateTimeFormatter formatter) {
			try {
				return LocalDate.parse(input, formatter);
			} catch (DateTimeParseException e) {
				Debugger.log(2, "DateParser with pattern [" + pattern + "] failed: " + e);
				return null;
			}
		}

		/**
		 * Constructor.
		 * @param pattern the full date pattern string to be parsed
		 **/
		public Base(String pattern) {
			this.pattern = pattern;
		}

		@Override
		public LocalDate parse(String input) {
			return parseWithFormatter(input, DateTimeFormatter.ofPattern(pattern));
		}
	}

	/**
	 * Parser for a date string.
	 *
	 * e.g. "Jul 4, 2021"
	 **/
	public static class Common extends Base {
		public Common() {
			super("MMM d, yyyy");
		}
	}
	/**
	 * Parser for a date string in the military style.
	 *
	 * e.g. "2021-7-5"
	 **/
	public static class Military extends Base {
		public Military() {
			super("yyyy-M-d");
		}
	}
}
