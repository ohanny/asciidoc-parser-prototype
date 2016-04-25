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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;

import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class IncludeReaderTest extends BaseParser {

    private ParseTreeListener listener;
    private InputBufferStateListener inputBufferStateListener;

    @Before
    public void init() {
        listener = mock(ParseTreeListener.class);
        inputBufferStateListener = mock(InputBufferStateListener.class); // TODO ???
    }

    @Parameterized.Parameter
    public String bufferType;

    @Parameterized.Parameters(name="{index}: {0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { StringInputBuffer.class.getSimpleName()}, { ReaderInputBuffer.class.getSimpleName()}
        });
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
        Answer<?> answer = invocation -> {
            Object[] args = invocation.getArguments();
            NodeContext ctx = (NodeContext)args[0];
            if ("input".equals(ctx.getNodeName())) {
                ctx.include(new StringReader("abc"));
            }
            return null;
        };
        doAnswer(answer).when(listener).enterNode(anyObject());

        Rule include = node("include", sequence(string("inc:"), node("input", oneOrMore(noneOf("&*")))));
        Rule rule = node("root", sequence(string("###"), include, optional(sequence(oneOrMore(any()), eoi(), oneOrMore(any()), eoi()))));
        //Rule rule = node("root", sequence(string("###"), include, optional(oneOrMore(any()))));
//        Rule rule = node("root", sequence(string("###"), include, optional(oneOrMore(anyOf("abc*")))));

        ParsingResult result = parse(rule, "###inc:input.txt***", listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, times(3)).enterNode(ac.capture());
        verify(listener).characters(aryEq("###".toCharArray()), anyInt(), anyInt());
        verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        verify(listener).characters(aryEq("input.txt".toCharArray()), anyInt(), anyInt());
        verify(listener).characters(aryEq("abc\uFFFF***\uFFFF".toCharArray()), anyInt(), anyInt());
    }
}
