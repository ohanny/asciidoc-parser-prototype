package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.listeners.ParsingProcessListener;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlushingTest extends BaseParser {
    private ParseTreeListener listener;
    private InputBufferStateListener inputBufferStateListener;

    @Before
    public void init() {
        listener = mock(ParseTreeListener.class);
        inputBufferStateListener = mock(InputBufferStateListener.class);
    }

    private ParsingResult parse(Rule rule, String text, ParseTreeListener parseTreeListener,
                                ParsingProcessListener parsingProcessListener, InputBufferStateListener bufferListener) {
        return new ParseRunner(this, () -> rule).parse(text, parseTreeListener, parsingProcessListener, bufferListener);
    }


    @Test
    public void test1() throws Exception {
        Rule rule = node("root", sequence(node("child", ch('a')), ch('b')));

        ParsingResult result = parse(rule, "ab", listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(inputBufferStateListener).visitNextChar(0, 'a');
        inOrder.verify(inputBufferStateListener).visitNextChar(1, 'b');
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(listener).characters(new char[] {'b'}, 1, 1);
        inOrder.verify(listener).exitNode("root");
    }

    @Test
    public void test2() throws Exception {
        Rule rule = node("root", sequence(node("child", ch('a')), optional(ch('b'))));

        ParsingResult result = parse(rule, "ab", listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(inputBufferStateListener).visitNextChar(0, 'a');
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(inputBufferStateListener).visitNextChar(1, 'b');
        inOrder.verify(listener).characters(new char[] {'b'}, 1, 1);
        inOrder.verify(listener).exitNode("root");
    }
}
