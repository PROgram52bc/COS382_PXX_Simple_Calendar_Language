package scl.parsers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Parser that converts a string into a {@link LocalDateTime} object
 **/
public class DateTimeParserNoYear extends MultiParser<LocalDateTime> {
	public DateTimeParserNoYear() {
		addParser(new Common());
		addParser(new Military());
	}

	/**
	 * A base class to parse datetime string with an absent year specification.
	 *
	 * The year's value will fallback to a default value. */
	public static class Base extends DateTimeParser.Base {

		protected long defaultYear;

		/**
		 * Constructor. Use the current year as the default year.
		 **/
		public Base(String pattern) {
			super(pattern);
			this.defaultYear = LocalDateTime.now().getYear();
		}
		/**
		 * Constructor. Use the specified year as the default year.
		 **/
		public Base(String pattern, long defaultYear) {
			super(pattern);
			this.defaultYear = defaultYear;
		}
		@Override
		public LocalDateTime parse(String input) {
			DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.appendPattern(pattern)
				.parseDefaulting(ChronoField.YEAR, defaultYear)
				.toFormatter();
			return parseWithFormatter(input, formatter);
		}
	}

	/**
	 * Parser for a datetime string.
	 *
	 * e.g. "Jul 4, 11:55 AM"
	 **/
	public static class Common extends Base {
		public Common() {
			super("MMM d, hh:mm a");
		}
	}

	/**
	 * Parser for a datetime string in the military style.
	 *
	 * e.g. "7-5 22:00"
	 **/
	public static class Military extends Base {
		public Military() {
			super("M-d HH:mm");
		}
	}
}
