package scl.parsers;

import java.util.LinkedList;

/**
 * A utility class to combine multiple parsers together.
 **/
public class MultiParser<T> implements Parser<T> {
    private final LinkedList<Parser<T>> parsers;

    public MultiParser() {
        parsers = new LinkedList<>();
    }

    /**
     * Adds a parser to the current {@link MultiParser}.
     * @param parser the parser to be added.
     * @return the current object.
     *
     * This is the primary way to construct a {@link MultiParser},
     * where the given parser is appended to the end of existing parsers.
     *
     * The method returns the object itself, so that multiple calls can be chained together:
     *
     * <pre>{@code
     * MultiParser parser = new MultiParser()
     *      .addParser(parser1)
     *      .addParser(parser2);
     * }
     * </pre>
     *
     * */
    public MultiParser<T> addParser(Parser<T> parser) {
        parsers.add(parser);
        return this;
    }

    /**
     * Parse the input with all registered parsers, return the result of the first one that is valid.
     * @param input the input string.
     *
	 * @return a parsed result, null if all parsers failed to parse the given input.
     **/
    public final T parse(String input) {
        for (Parser<T> parser: parsers) {
            T result = parser.parse(input);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

}
