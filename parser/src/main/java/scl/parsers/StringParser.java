package scl.parsers;

/** 
 * The main interface for parsers used in {@link AttributesHandler}
 **/
interface StringParser<T> {
    /**
     * Parses a string value into a {@link T} object.
	 * @param input the input string.
     *
	 * @return the parsed object, returns null if the parse failed.
     **/
    T parse(String input);
}
