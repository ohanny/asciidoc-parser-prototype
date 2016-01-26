package fr.icodem.asciidoc.parser.peg;

/**
 * A simple container encapsulating the result of a parsing run.
 */
public class ParsingResult {
    /**
     * Indicates whether the input was successfully parsed.
     */
    public final boolean matched;

    /**
     * Creates a new ParsingResult.
     *
     * @param matched true if the rule matched the input
     */
    public ParsingResult(boolean matched) {
        this.matched = matched;
    }
}
