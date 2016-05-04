package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.buffers.StringHolder;
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
import org.mockito.InOrder;
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
        inputBufferStateListener = mock(InputBufferStateListener.class);
    }

    @Parameterized.Parameter
    public String bufferType;

    @Parameterized.Parameters(name="{index}: {0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "string" }, { "reader" }
        });
    }


    private ParsingResult parse(Rule rule, String text, ParseTreeListener parseTreeListener,
                                ParsingProcessListener parsingProcessListener, InputBufferStateListener bufferListener) {
        if ("string".equals(bufferType)) {
            return new ParseRunner(this, () -> rule).parse(text, parseTreeListener, parsingProcessListener, bufferListener);
        }
        else if ("reader".equals(bufferType)) {
            return new ParseRunner(this, () -> rule).parse(new StringReader(text), parseTreeListener, parsingProcessListener, bufferListener);
        }
        return null;
    }

    @Test // include made by listener : one include, no reset
    public void test1() throws Exception {
        Answer<?> answer = invocation -> {
            Object[] args = invocation.getArguments();
            NodeContext ctx = (NodeContext)args[0];
            if ("input".equals(ctx.getNodeName())) {
                if ("string".equals(bufferType)) {
                    ctx.include(new StringHolder("abc"));
                }
                else if ("reader".equals(bufferType)) {
                    ctx.include(new StringReader("abc"));
                }
            }
            return null;
        };
        doAnswer(answer).when(listener).enterNode(anyObject());

        Rule include = node("include", sequence(string("inc:"), node("input", oneOrMore(noneOf("&*")))));
        Rule rule = node("root", sequence(string("###"), include, optional(sequence(oneOrMore(any()), eoi(), oneOrMore(any()), eoi()))));

        ParsingResult result = parse(rule, "###inc:input.txt***", listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, times(3)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input.txt".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("abc\uFFFF***\uFFFF".toCharArray()), anyInt(), anyInt());
    }

    // ************************************
    // includes made at rule level
    // ************************************

    @Test // one include, no reset
    public void test2() throws Exception {
        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("&*")), ctx -> {
                    char[] chars = ctx.extract();
                    if ("string".equals(bufferType)) {
                        ctx.include(new StringHolder("abc"));
                    }
                    else if ("reader".equals(bufferType)) {
                        ctx.include(new StringReader("abc"));
                    }
                })
        )));
        Rule rule = node("root", sequence(string("###"), include, optional(sequence(oneOrMore(any()), eoi(), oneOrMore(any()), eoi()))));

        ParsingResult result = parse(rule, "###inc:input.txt***", listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, times(3)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input.txt".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("abc\uFFFF***\uFFFF".toCharArray()), anyInt(), anyInt());
    }

    // one include, with reset inside second source (position back to second source)
    @Test
    public void test3() throws Exception {
        String source1 = "###inc:input.txt***";
        String source2 = "abd";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("abcd*")), ctx -> {
                    if ("string".equals(bufferType)) {
                        ctx.include(new StringHolder(source2));
                    }
                    else if ("reader".equals(bufferType)) {
                        ctx.include(new StringReader(source2));
                    }
                })
        )));
        Rule rule = node("root", sequence(string("###"), include,
                firstOf(string("abc"), string("abd")), eoi(), string("***")));

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, times(3)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input.txt".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("abd\uFFFF***".toCharArray()), anyInt(), anyInt());
        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // one include, with reset inside first source (position back to first source)
    @Test
    public void test4() throws Exception {
        String source1 = "###inc:input.txt***";
        String source2 = "abd";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("abcd*")), ctx -> {
                    if ("string".equals(bufferType)) {
                        ctx.include(new StringHolder(source2));
                    }
                    else if ("reader".equals(bufferType)) {
                        ctx.include(new StringReader(source2));
                    }
                })
        )));
        Rule rule = node("root", firstOf(
                sequence(string("###"), include, string("abc"), eoi(), string("***")),
                sequence(string("###"), include, string("abd"), eoi(), string("***"))
        ));

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, times(3)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input.txt".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("abd\uFFFF***".toCharArray()), anyInt(), anyInt());
        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, times(1)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // two sequential includes, no reset
    @Test
    public void test5() throws Exception {
        String source1 = "###inc:input1***inc:input2&&&";
        String source2 = "abc";
        String source3 = "xyz";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("abcdxyz*#&")), ctx -> {
                    String srcName = new String(ctx.extract());
                    if ("string".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringHolder(source2));
                        }
                        else if ("input2".equals(new String(ctx.extract()))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(ctx.extract()))) {
                            ctx.include(new StringReader(source3));
                        }
                    }
                })
        )));
        Rule rule = node("root", sequence(
                                    string("###"),
                                    include,
                                    string("abc"),
                                    eoi(),
                                    string("***"),
                                    include,
                                    string("xyz"),
                                    eoi(),
                                    string("&&&"),
                                    eoi()
                                 )
                        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("abc\uFFFF***".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("xyz\uFFFF&&&\uFFFF".toCharArray()), anyInt(), anyInt());
        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // two sequential includes, with reset inside second include
    @Test
    public void test6() throws Exception {
        String source1 = "###inc:input1***inc:input2&&&";
        String source2 = "abc";
        String source3 = "xyw";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("abcdxyz*#&")), ctx -> {
                    String srcName = new String(ctx.extract());
                    if ("string".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringHolder(source2));
                        }
                        else if ("input2".equals(new String(ctx.extract()))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(ctx.extract()))) {
                            ctx.include(new StringReader(source3));
                        }
                    }
                })
        )));
        Rule rule = node("root", sequence(
                string("###"),
                include,
                string("abc"),
                eoi(),
                string("***"),
                include,
                firstOf(string("xyz"), string("xyw")),
                eoi(),
                string("&&&"),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("abc\uFFFF***".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("xyw\uFFFF&&&\uFFFF".toCharArray()), anyInt(), anyInt());
        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // two sequential includes, with reset just before second include
    @Test
    public void test7() throws Exception {
        String source1 = "###inc:input1***inc:input2&&&";
        String source2 = "abc";
        String source3 = "xyz";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("abcdxyz*#&")), ctx -> {
                    String srcName = new String(ctx.extract());
                    if ("string".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringHolder(source2));
                        }
                        else if ("input2".equals(new String(ctx.extract()))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(ctx.extract()))) {
                            ctx.include(new StringReader(source3));
                        }
                    }
                })
        )));
        Rule rule = node("root", sequence(
                string("###"),
                include,
                string("abc"),
                eoi(),
                string("***"),
                include,
                firstOf(sequence(string("xyz"), eoi(), string("&#&")), sequence(string("xyz"), eoi(), string("&&&"))),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("abc\uFFFF***".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("xyz\uFFFF&&&\uFFFF".toCharArray()), anyInt(), anyInt());
        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
    }


    @Test // two includes, the second one is nested in the first one, no reset
    public void test8() throws Exception {
        String source1 = "###inc:input1***";
        String source2 = "&&&inc:input2@@@";
        String source3 = "abc";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("abc*#&@")), ctx -> {
                    String srcName = new String(ctx.extract());
                    System.out.println(srcName);
                    if ("string".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringHolder(source2));
                        }
                        else if ("input2".equals(new String(ctx.extract()))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(ctx.extract()))) {
                            ctx.include(new StringReader(source3));
                        }
                    }
                })
        )));
        Rule rule = node("root", sequence(
                string("###"),
                include,
                string("&&&"),
                include,
                string("abc"),
                eoi(),
                string("@@@"),
                eoi(),
                string("***"),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("&&&".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(aryEq("abc\uFFFF@@@\uFFFF***\uFFFF".toCharArray()), anyInt(), anyInt());
        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, times(2)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // two includes, the second one is nested in the first one, with reset inside first source
    // two includes, the second one is nested in the first one, with reset inside second source
    // two includes, the second one is nested in the first one, with reset inside third source

    //
}
