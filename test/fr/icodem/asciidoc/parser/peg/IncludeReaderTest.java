package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.buffers.StringHolder;
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
public class IncludeReaderTest extends BaseRules {

    private ParseTreeListener listener;
    private InputBufferStateListener inputBufferStateListener;

    @Before
    public void init() {
        withFactory(RulesFactory.defaultRulesFactory());
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

    // ************************************
    // includes made at listener level
    // ************************************

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

        Rule include = node("include", sequence(string("inc:"),
                node("input", oneOrMore(noneOf("&*"))
                ))
        );
        Rule rule = node("root", sequence(string("###"), include, optional(sequence(oneOrMore(any()), eoi()))));

        ParsingResult result = parse(rule, "###inc:input.txt***", listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(3)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input.txt".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abc***\uFFFF".toCharArray()), anyInt(), anyInt());

        verify(inputBufferStateListener).visitReset(15, 16, 0);
        verify(inputBufferStateListener).visitReset(21, 6, 16);
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(anyInt(), anyInt(), anyInt());
    }

    // ************************************
    // includes made at rule level
    // ************************************

    @Test // one include, no reset
    public void test2() throws Exception {
        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("&*")), ctx -> {
                    if ("string".equals(bufferType)) {
                        ctx.include(new StringHolder("abc"));
                    }
                    else if ("reader".equals(bufferType)) {
                        ctx.include(new StringReader("abc"));
                    }
                })
        )));
        Rule rule = node("root", sequence(string("###"), include, optional(sequence(oneOrMore(any()), eoi()))));

        ParsingResult result = parse(rule, "###inc:input.txt***", listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(3)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input.txt".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abc***\uFFFF".toCharArray()), anyInt(), anyInt());

        verify(inputBufferStateListener).visitReset(15, 16, 0);
        verify(inputBufferStateListener).visitReset(21, 6, 16);
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(anyInt(), anyInt(), anyInt());
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
                firstOf(string("abc"), string("abd")), string("***")));

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(3)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input.txt".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abd***".toCharArray()), anyInt(), anyInt());
        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener).visitReset(15, 16, 0);
        verify(inputBufferStateListener).visitReset(15, 18, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(anyInt(), anyInt(), anyInt());
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
                sequence(string("###"), include, string("abc"), string("***")),
                sequence(string("###"), include, string("abd"), string("***"))
        ));

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(3)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input.txt".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abd***".toCharArray()), anyInt(), anyInt());

        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, org.mockito.Mockito.times(1)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(15, 16, 0);
        verify(inputBufferStateListener).visitReset(-1, 18, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(3)).visitReset(anyInt(), anyInt(), anyInt());
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
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringReader(source3));
                        }
                    }
                })
        )));
        Rule rule = node("root", sequence(
                                    string("###"),
                                    include,
                                    string("abc"),
                                    string("***"),
                                    include,
                                    string("xyz"),
                                    string("&&&"),
                                    eoi()
                                 )
                        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abc***".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("xyz&&&\uFFFF".toCharArray()), anyInt(), anyInt());

        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener).visitReset(12, 13, 0);
        verify(inputBufferStateListener).visitReset(28, 29, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(anyInt(), anyInt(), anyInt());
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
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringReader(source3));
                        }
                    }
                })
        )));
        Rule rule = node("root", sequence(
                string("###"),
                include,
                string("abc"),
                string("***"),
                include,
                firstOf(string("xyz"), string("xyw")),
                string("&&&"),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abc***".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("xyw&&&\uFFFF".toCharArray()), anyInt(), anyInt());

        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }

        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener).visitReset(12, 13, 0);
        verify(inputBufferStateListener).visitReset(28, 29, 0);
        verify(inputBufferStateListener).visitReset(28, 31, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(3)).visitReset(anyInt(), anyInt(), anyInt());
    }

    // two sequential includes, with reset just before the third source
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
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringReader(source3));
                        }
                    }
                })
        )));
        Rule rule = node("root", sequence(
                string("###"),
                include,
                string("abc"),
                string("***"),
                include,
                firstOf(sequence(string("xyz"), string("&#&")), sequence(string("xyz"), string("&&&"))),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abc***".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("xyz&&&\uFFFF".toCharArray()), anyInt(), anyInt());

        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }

        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener).visitReset(12, 13, 0);
        verify(inputBufferStateListener).visitReset(28, 29, 0);
        verify(inputBufferStateListener).visitReset(28, 33, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(3)).visitReset(anyInt(), anyInt(), anyInt());
    }

    @Test // two includes, the second one is nested in the first one, no reset
    public void test8() throws Exception {
        String source1 = "###inc:input1***";
        String source2 = "&&&inc:input2@@@";
        String source3 = "abd";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("abc*#&@")), ctx -> {
                    String srcName = new String(ctx.extract());
                    if ("string".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringHolder(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
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
                firstOf(string("abc"), string("abd")),
                string("@@@"),
                string("***"),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("&&&".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abd@@@***\uFFFF".toCharArray()), anyInt(), anyInt());

        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener).visitReset(12, 13, 0);
        verify(inputBufferStateListener).visitReset(25, 26, 0);
        verify(inputBufferStateListener).visitReset(25, 28, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(3)).visitReset(anyInt(), anyInt(), anyInt());
    }

    // two includes, the second one is nested in the first one, with reset inside third source
    @Test
    public void test9() throws Exception {
        String source1 = "###inc:input1***";
        String source2 = "&&&inc:input2@@@";
        String source3 = "abc";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("abc*#&@")), ctx -> {
                    String srcName = new String(ctx.extract());
                    if ("string".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringHolder(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
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
                string("@@@"),
                string("***"),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("&&&".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abc@@@***\uFFFF".toCharArray()), anyInt(), anyInt());

        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }

        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener).visitReset(12, 13, 0);
        verify(inputBufferStateListener).visitReset(25, 26, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(anyInt(), anyInt(), anyInt());
    }

    // two includes, the second one is nested in the first one, with reset just before the third source
    @Test
    public void test10() throws Exception {
        String source1 = "###inc:input1***";
        String source2 = "&&&inc:input2@@@";
        String source3 = "abc";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("abc*#&@")), ctx -> {
                    String srcName = new String(ctx.extract());
                    if ("string".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringHolder(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
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
                firstOf(sequence(string("abc"), string("@#@")), sequence(string("abc"), string("@@@"))),
                string("***"),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("&&&".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abc@@@***\uFFFF".toCharArray()), anyInt(), anyInt());

        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener).visitReset(12, 13, 0);
        verify(inputBufferStateListener).visitReset(25, 26, 0);
        verify(inputBufferStateListener).visitReset(25, 30, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(3)).visitReset(anyInt(), anyInt(), anyInt());
    }

    // two includes, the second one is nested in the first one, with reset inside second source
    @Test
    public void test11() throws Exception {
        String source1 = "###inc:input1***";
        String source2 = "&&&inc:input2@@@";
        String source3 = "abc";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("*#&@")), ctx -> {
                    String srcName = new String(ctx.extract());
                    if ("string".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringHolder(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringReader(source3));
                        }
                    }
                })
        )));
        Rule rule = node("root", sequence(
                string("###"),
                include,
                string("&&&"),
                firstOf(sequence(include, string("abc"), string("@#@")), sequence(include, string("abc"), string("@@@"))),
                string("***"),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("&&&".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abc@@@***\uFFFF".toCharArray()), anyInt(), anyInt());

        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener).visitReset(12, 13, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(25, 26, 0);
        verify(inputBufferStateListener).visitReset(15, 30, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(4)).visitReset(anyInt(), anyInt(), anyInt());
    }

    // two includes, the second one is nested in the first one, with reset just before the second source
    @Test
    public void test12() throws Exception {
        String source1 = "###inc:input1***";
        String source2 = "&&&inc:input2@@@";
        String source3 = "abc";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("*#&@")), ctx -> {
                    String srcName = new String(ctx.extract());
                    if ("string".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringHolder(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringReader(source3));
                        }
                    }
                })
        )));
        Rule rule = node("root", sequence(
                string("###"),
                include,
                firstOf(sequence(string("&&&"), include, string("abc"), string("@#@")), sequence(string("&&&"), include, string("abc"), string("@@@"))),
                string("***"),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("&&&".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abc@@@***\uFFFF".toCharArray()), anyInt(), anyInt());

        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener).visitReset(12, 13, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(25, 26, 0);
        verify(inputBufferStateListener).visitReset(12, 30, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(4)).visitReset(anyInt(), anyInt(), anyInt());
    }

    // two includes, the second one is nested in the first one, with reset inside first source
    @Test
    public void test13() throws Exception {
        String source1 = "###inc:input1***";
        String source2 = "&&&inc:input2@@@";
        String source3 = "abc";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("*#&@")), ctx -> {
                    String srcName = new String(ctx.extract());
                    if ("string".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringHolder(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringReader(source3));
                        }
                    }
                })
        )));
        Rule rule = node("root", sequence(
                string("###"),
                firstOf(
                  sequence(
                    include,
                    string("&&&"),
                    include,
                    string("abc"),
                    string("@#@")
                  ),
                  sequence(
                    include,
                    string("&&&"),
                    include,
                    string("abc"),
                    string("@@@")
                  )
                ),
                string("***"),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("&&&".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abc@@@***\uFFFF".toCharArray()), anyInt(), anyInt());

        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(12, 13, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(25, 26, 0);
        verify(inputBufferStateListener).visitReset(2, 30, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(5)).visitReset(anyInt(), anyInt(), anyInt());
    }

    // two includes, the second one is nested in the first one, with reset just before the first source
    @Test
    public void test14() throws Exception {
        String source1 = "###inc:input1***";
        String source2 = "&&&inc:input2@@@";
        String source3 = "abc";

        Rule include = node("include", sequence(string("inc:"), node("input",
                action(oneOrMore(noneOf("*#&@")), ctx -> {
                    String srcName = new String(ctx.extract());
                    if ("string".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringHolder(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringHolder(source3));
                        }
                    }
                    else if ("reader".equals(bufferType)) {
                        if ("input1".equals(srcName)) {
                            ctx.include(new StringReader(source2));
                        }
                        else if ("input2".equals(new String(srcName))) {
                            ctx.include(new StringReader(source3));
                        }
                    }
                })
        )));
        Rule rule = node("root", sequence(
                firstOf(sequence(string("###"), include, string("&&&"), include, string("abc"), string("@#@")), sequence(string("###"), include, string("&&&"), include, string("abc"), string("@@@"))),
                string("***"),
                eoi()
                )
        );

        ParsingResult result = parse(rule, source1, listener, null, inputBufferStateListener);

        assertTrue("Did not match", result.matched);

        ArgumentCaptor<NodeContext> ac = ArgumentCaptor.forClass(NodeContext.class);
        verify(listener, org.mockito.Mockito.times(5)).enterNode(ac.capture());

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).characters(ac.capture(), aryEq("###".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input1".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("&&&".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("inc:".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("input2".toCharArray()), anyInt(), anyInt());
        inOrder.verify(listener).characters(ac.capture(), aryEq("abc@@@***\uFFFF".toCharArray()), anyInt(), anyInt());

        if ("string".equals(bufferType)) {
            verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
        }
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());

        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(12, 13, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(2)).visitReset(25, 26, 0);
        verify(inputBufferStateListener).visitReset(-1, 30, 0);
        verify(inputBufferStateListener, org.mockito.Mockito.times(5)).visitReset(anyInt(), anyInt(), anyInt());
    }

}
