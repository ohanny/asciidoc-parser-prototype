package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.buffers.InputBufferBuilder.BufferType;
import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.listeners.ParsingProcessListener;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.io.StringReader;
import java.util.Arrays;

import static fr.icodem.asciidoc.parser.peg.buffers.InputBufferBuilder.BufferType.Reader;
import static fr.icodem.asciidoc.parser.peg.buffers.InputBufferBuilder.BufferType.String;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class FlushingTest extends BaseRules {
    private ParseTreeListener listener;
    private InputBufferStateListener inputBufferStateListener;

    private ArgumentCaptor<NodeContext> ac;

    @Parameter
    public BufferType bufferType;

    @Parameters(name="{index}: {0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {String}, { Reader}
        });
    }

    @Before
    public void init() {
        useFactory(RulesFactory.defaultRulesFactory());
        listener = mock(ParseTreeListener.class);
        inputBufferStateListener = mock(InputBufferStateListener.class);
        ac = ArgumentCaptor.forClass(NodeContext.class);
    }

    private ParsingResult parse(Rule rule, String text, ParseTreeListener parseTreeListener,
                                ParsingProcessListener parsingProcessListener, InputBufferStateListener bufferListener) {

        if (String.equals(bufferType)) {
            return new ParseRunner(this, () -> rule).parse(text, parseTreeListener, parsingProcessListener, bufferListener);
        }
        else if (Reader.equals(bufferType)) {
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
        inOrder.verify(listener).characters(ac.capture(), aryEq(new char[]{'a'}), eq(0), eq(0));
        inOrder.verify(listener).exitNode(ac.capture());
        assertEquals("Node name incorrect", "child", ac.getValue().getNodeName());
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).characters(ac.capture(), aryEq(new char[]{'b'}), eq(1), eq(1));
        inOrder.verify(listener).exitNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        verify(inputBufferStateListener, never()).visitReset(anyInt(), anyInt(), anyInt());
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
        inOrder.verify(listener).characters(ac.capture(), aryEq(new char[]{'a'}), eq(0), eq(0));
        inOrder.verify(listener).exitNode(ac.capture());
        assertEquals("Node name incorrect", "child", ac.getValue().getNodeName());
        inOrder.verify(inputBufferStateListener).visitNextChar(1, 'b');
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).characters(ac.capture(), aryEq(new char[]{'b'}), eq(1), eq(1));
        inOrder.verify(listener).exitNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        verify(inputBufferStateListener, never()).visitReset(anyInt(), anyInt(), anyInt());
    }

}