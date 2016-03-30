package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.listeners.ParsingProcessListener;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InOrder;

import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class FlushingWithReaderInputBufferTest extends BaseParser {
    private ParseTreeListener listener;
    private InputBufferStateListener inputBufferStateListener;

    @Before
    public void init() {
        listener = mock(ParseTreeListener.class);
        inputBufferStateListener = mock(InputBufferStateListener.class);
    }

    private ParsingResult parse(Rule rule, String text, ParseTreeListener parseTreeListener,
                                ParsingProcessListener parsingProcessListener,
                                InputBufferStateListener bufferListener,
                                int bufferSize) {

        return new ParseRunner(this, () -> rule).parse(new StringReader(text),
                parseTreeListener, parsingProcessListener, bufferListener, bufferSize);
    }

    @Test
    public void test1() throws Exception {
        Rule rule = node("root", sequence(ch('a'), node("child", ch('b')), optional('c')));

        ParsingResult result = parse(rule, "abc", listener, null, inputBufferStateListener, 100);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(inputBufferStateListener).visitNextChar(0, 'a');
        inOrder.verify(inputBufferStateListener).visitNextChar(1, 'b');
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child");

        char[] data = new char[100];
        data[0] = 'c';
        data[1] = 'b';
        data[2] = 'c';
        inOrder.verify(inputBufferStateListener).visitData("consume", data, 1, -1, 2);
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'c'}, 2, 2);
        inOrder.verify(listener).characters(new char[]{'c'}, 2, 2);
        inOrder.verify(listener).exitNode("root");
        inOrder.verify(inputBufferStateListener).visitData("consume", data, 0, -1, 3);
    }

    @Test
    public void test2() throws Exception {
        Rule rule = node("root", sequence(ch('a'), zeroOrMore(node("child", ch('b'))), optional('c')));

        ParsingResult result = parse(rule, "abbbbbbc", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(inputBufferStateListener).visitNextChar(0, 'a');
        inOrder.verify(inputBufferStateListener).visitNextChar(1, 'b');
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child");

        inOrder.verify(inputBufferStateListener).visitNextChar(2, 'b');
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'b'}, 2, 2);
        inOrder.verify(listener).characters(new char[]{'b'}, 2, 2);
        inOrder.verify(listener).exitNode("child");

        inOrder.verify(inputBufferStateListener).visitNextChar(3, 'b');
        inOrder.verify(inputBufferStateListener).visitNextChar(4, 'b');
        inOrder.verify(inputBufferStateListener).visitNextChar(5, 'b');
        inOrder.verify(inputBufferStateListener).visitNextChar(6, 'b');

//        char[] data = new char[1024];
//        data[0] = 'c';
//        data[1] = 'b';
//        data[2] = 'c';
//        inOrder.verify(inputBufferStateListener).visitData("consume", data, 1, -1, 2);
//        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'c'}, 2, 2);
//        inOrder.verify(listener).characters(new char[]{'c'}, 2, 2);
//        inOrder.verify(listener).exitNode("root");
//        inOrder.verify(inputBufferStateListener).visitData("consume", data, 0, -1, 3);
    }

}