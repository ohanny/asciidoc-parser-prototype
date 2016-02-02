package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.listeners.*;
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
    public ParsingResult parse(String text, ParseTreeListener parseTreeListener,
                               ParsingProcessListener parsingProcessListener,
                               InputBufferStateListener inputBufferStateListener) {

        if (parseTreeListener == null) {
            parseTreeListener = new DefaultParseTreeListener();
        }
        if (parsingProcessListener == null) {
            parsingProcessListener = new DefaultParsingProcessListener();
        }
        if (inputBufferStateListener == null) {
            inputBufferStateListener = new DefaultInputBufferStateListener();
        }

        InputBuffer input = new InputBuffer(text, inputBufferStateListener);

        Matcher matcher = rule.getMatcher();

        boolean matched = matcher.match(new MatcherContext(input, parseTreeListener, parsingProcessListener));

        ParsingResult result = new ParsingResult(matched);

        return result;

    }

    public ParsingResult parse(String text, ParseTreeListener parseTreeListener,
                               ParsingProcessListener parsingProcessListener) {
        return parse(text, parseTreeListener,
                parsingProcessListener, new DefaultInputBufferStateListener());
    }

    public ParsingResult parse(String text, ParseTreeListener parseTreeListener) {
        return parse(text, parseTreeListener,
                new DefaultParsingProcessListener(), new DefaultInputBufferStateListener());
    }

    public ParsingResult parse(String text) {
        return parse(text, new DefaultParseTreeListener(),
                new DefaultParsingProcessListener(),
                new DefaultInputBufferStateListener());
    }

}
