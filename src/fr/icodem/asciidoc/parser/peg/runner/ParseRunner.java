package fr.icodem.asciidoc.parser.peg.runner;

import fr.icodem.asciidoc.parser.peg.BaseParser;
import fr.icodem.asciidoc.parser.peg.InputBuffer;
import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.StringInputBuffer;
import fr.icodem.asciidoc.parser.peg.listeners.*;
import fr.icodem.asciidoc.parser.peg.matchers.Matcher;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.RuleSupplier;

/**
 * A ParseRunner performs the actual parsing run of a given parser rule on a given input text.
 */
public class ParseRunner {

    private BaseParser parser;
    private RuleSupplier ruleSupplier;
    private boolean generateStringTree;
    private boolean trace;

    public ParseRunner(BaseParser parser, RuleSupplier ruleSupplier) {
        this.parser = parser;
        this.ruleSupplier = ruleSupplier;

        if (ruleSupplier == null) {
            throw new IllegalArgumentException("A rule supplier must be provided");
        }

    }

    public ParseRunner generateStringTree() {
        this.generateStringTree = true;
        return this;
    }

    public ParseRunner trace() {
        this.trace = true;
        return this;
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

        if (generateStringTree) {
            parseTreeListener = new ToStringTreeBuilder();
        }
        if (trace) {
            parsingProcessListener = new ToStringAnalysisBuilder();
            parser.useSpyingRulesFactory();
        }

        InputBuffer input = InputBuffer.stringInputBuffer(text, inputBufferStateListener);

        Rule rule = ruleSupplier.getRule();
        Matcher matcher = rule.getMatcher();

        boolean matched = matcher.match(new MatcherContext(input, parseTreeListener, parsingProcessListener));

        ParsingResult result;
        if (generateStringTree) {
            String tree = ((ToStringTreeBuilder)parseTreeListener).getStringTree();
            result = new ParsingResult(matched, tree);
        } else {
            result = new ParsingResult(matched);
        }

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
