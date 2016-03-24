package fr.icodem.asciidoc.parser.peg.runner;

/**
 * A simple container encapsulating the result of a parsing run.
 */
public class ParsingResult {
    /**
     * Indicates whether the input was successfully parsed.
     */
    public final boolean matched;

    /**
     * The string tree in LISP style text form, if it were requested to produce it
     */
    public final String tree;

    /**
     * Creates a new ParsingResult.
     *
     * @param matched true if the rule matched the input
     */
    public ParsingResult(boolean matched) {
        this.matched = matched;
        this.tree = null;
    }

    /**
     * Creates a new ParsingResult with string tree.
     *
     * @param matched true if the rule matched the input
     * @param tree the string tree
     */
    public ParsingResult(boolean matched, String tree) {
        this.matched = matched;
        this.tree = tree;
    }
}
