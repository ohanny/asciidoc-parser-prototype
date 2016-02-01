package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

/**
 * A ParseRunner performs the actual parsing run of a given parser rule on a given input text.
 */
public class ParseRunner {

    private final Rule rule;

    public ParseRunner(Rule rule) {
        this.rule = rule;
    }

    /**
     * Performs the actual parse and creates a corresponding ParsingResult instance.
     *
     * @param text the input text to parse
     * @return the ParsingResult for the run
     */
    public ParsingResult parse(String text, ParseTreeListener listener) {

        InputBuffer input = new InputBuffer(text);

        Matcher matcher = rule.getMatcher();

        boolean matched = matcher.match(new MatcherContext(input, listener));

        ParsingResult result = new ParsingResult(matched);

        return result;

    }

    public ParsingResult parse(String text) {
        return parse(text, null);
    }

}
