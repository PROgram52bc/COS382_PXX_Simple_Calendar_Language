package scl.parsers;

import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class ListParser implements Parser<List<String>> {

	private String delimiter;
	private boolean trim;

	/**
	 * Constructor.
	 * @param delimiter the separator to split a string into lists, can be a {@link Regex} pattern.
	 * @param trim whether to trim the spaces at the end of each item.
	 **/
	public ListParser(String delimiter, boolean trim) {
		this.delimiter = delimiter;
		this.trim = trim;
	}

	@Override
	public List<String> parse(String input) {
		List<String> parts = new LinkedList<String>(Arrays.asList(input.split(delimiter)));
		if (trim) {
			parts = parts
				.stream()
				.map(String::trim)
				.collect(Collectors.toList());
		}
		return parts;
	}
}
