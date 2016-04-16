package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.buffers.ReaderInputBuffer;
import fr.icodem.asciidoc.parser.peg.buffers.StringInputBuffer;
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
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;

import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class FlushingTest extends BaseParser {
    private ParseTreeListener listener;
    private InputBufferStateListener inputBufferStateListener;

    private ArgumentCaptor<NodeContext> ac;

    @Parameter
    public String bufferType;

    @Parameters(name="{index}: {0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { StringInputBuffer.class.getSimpleName()}, { ReaderInputBuffer.class.getSimpleName()}
        });
    }

    @Before
    public void init() {
        listener = mock(ParseTreeListener.class);
        inputBufferStateListener = mock(InputBufferStateListener.class);
        ac = ArgumentCaptor.forClass(NodeContext.class);
    }

    private ParsingResult parse(Rule rule, String text, ParseTreeListener parseTreeListener,
                                ParsingProcessListener parsingProcessListener, InputBufferStateListener bufferListener) {

        if (StringInputBuffer.class.getSimpleName().equals(bufferType)) {
            return new ParseRunner(this, () -> rule).parse(text, parseTreeListener, parsingProcessListener, bufferListener);
        }
        else if (ReaderInputBuffer.class.getSimpleName().equals(bufferType)) {
            return new ParseRunner(this, () -> rule).parse(new StringReader(text), parseTreeListener, parsingProcessListener, bufferListener);
        }

        return null;
    }

    @Test
    public void test1() throws Exception {
        Rule rule = node("root", sequence(node("child", ch('a')), ch('b')));

        ParsingResult result = parse(rule, "ab", listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(inputBufferStateListener).visitNextChar(0, 'a');
        inOrder.verify(inputBufferStateListener).visitNextChar(1, 'b');

        inOrder.verify(listener, times(2)).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getAllValues().get(0).getNodeName());
        assertEquals("Node name incorrect", "child", ac.getAllValues().get(1).getNodeName());

        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("root");
    }

    @Test
    public void test2() throws Exception {
        Rule rule = node("root", sequence(node("child", ch('a')), optional(ch('b'))));

        ParsingResult result = parse(rule, "ab", listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(inputBufferStateListener).visitNextChar(0, 'a');

        inOrder.verify(listener, times(2)).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getAllValues().get(0).getNodeName());
        assertEquals("Node name incorrect", "child", ac.getAllValues().get(1).getNodeName());

        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(inputBufferStateListener).visitNextChar(1, 'b');
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("root");
    }

}