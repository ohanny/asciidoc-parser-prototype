package fr.icodem.asciidoc.parser.peg.runner;

import fr.icodem.asciidoc.parser.peg.BaseParser;
import fr.icodem.asciidoc.parser.peg.buffers.InputBuffer;
import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.buffers.InputBufferBuilder;
import fr.icodem.asciidoc.parser.peg.listeners.*;
import fr.icodem.asciidoc.parser.peg.matchers.Matcher;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.RuleSupplier;

import java.io.Reader;

/**
 * A ParseRunner performs the actual parsing run of a given parser rule on a given input text.
 */
public class ParseRunner {

    private BaseParser parser;
    private RuleSupplier ruleSupplier;
    private boolean generateStringTree;
    private boolean trace;
    private int bufferSize; // initial buffer size used with ReaderInputBuffer

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

    public ParseRunner bufferSize(int size) {
        this.bufferSize = size;
        return this;
    }

    public ParsingResult parse(Reader reader, ParseTreeListener parseTreeListener,
                               ParsingProcessListener parsingProcessListener,
                               InputBufferStateListener inputBufferStateListener,
                               int bufferSize) {
        InputBuffer input = InputBufferBuilder.readerInputBuffer(reader)
                                       .bufferSize(bufferSize)
                                       .useListener(inputBufferStateListener)
                                       .build();

        ParsingResult result = parse(parseTreeListener, parsingProcessListener, input);

        return result;
    }

        /**
         * Performs the actual parse and creates a corresponding ParsingResult instance.
         *
         * @param reader the input reader to parse
         * @return the ParsingResult for the run
         */
    public ParsingResult parse(Reader reader, ParseTreeListener parseTreeListener,
                               ParsingProcessListener parsingProcessListener,
                               InputBufferStateListener inputBufferStateListener) {

        InputBuffer input = InputBufferBuilder.readerInputBuffer(reader)
                                       .useListener(inputBufferStateListener)
                                       .build();

        ParsingResult result = parse(parseTreeListener, parsingProcessListener, input);

        return result;

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

        InputBuffer input = InputBufferBuilder.stringInputBuffer(text)
                                       .useListener(inputBufferStateListener)
                                       .build();

        ParsingResult result = parse(parseTreeListener, parsingProcessListener, input);

        return result;

    }

    public ParsingResult parse(String text, ParseTreeListener parseTreeListener,
                               ParsingProcessListener parsingProcessListener) {
        return parse(text, parseTreeListener, parsingProcessListener, null);
    }

    public ParsingResult parse(String text, ParseTreeListener parseTreeListener) {
        return parse(text, parseTreeListener, new DefaultParsingProcessListener(), null);
    }

    public ParsingResult parse(String text) {
        return parse(text, new DefaultParseTreeListener(), new DefaultParsingProcessListener(), null);
    }

    private ParsingResult parse(ParseTreeListener parseTreeListener, ParsingProcessListener parsingProcessListener, InputBuffer input) {
        if (parseTreeListener == null) {
            parseTreeListener = new DefaultParseTreeListener();
        }
        if (parsingProcessListener == null) {
            parsingProcessListener = new DefaultParsingProcessListener();
        }

        if (generateStringTree) {
            parseTreeListener = new ToStringTreeBuilder();
        }
        if (trace) {
            parsingProcessListener = new ToStringAnalysisBuilder();
            parser.useSpyingRulesFactory();
        }


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


}
