package scl.parsers;

import biweekly.util.Frequency;

/**
 * Parser that converts a string into a {@link Frequency} object.
 **/
public class FrequencyParser implements Parser<Frequency> {
	
	@Override
	public Frequency parse(String input) {
		// SECONDLY
		// MINUTELY
		// HOURLY
		// DAILY
		// WEEKLY
		// MONTHLY
		// YEARLY
		switch (input) {
			case "week":
			case "weeks":
			case "Week":
			case "Weeks":
				return Frequency.WEEKLY;
				// case "...":
				// 	return Frequency.XXX;
			default:
				return null;
		}
	}
}
