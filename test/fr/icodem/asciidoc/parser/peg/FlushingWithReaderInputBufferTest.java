package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.listeners.ParsingProcessListener;
import fr.icodem.asciidoc.parser.peg.matchers.OptionalMatcher;
import fr.icodem.asciidoc.parser.peg.matchers.SpyingMatcher;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.AdditionalMatchers;
import org.mockito.InOrder;
import org.mockito.internal.stubbing.answers.ClonesArguments;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class FlushingWithReaderInputBufferTest extends BaseParser {
    private ParseTreeListener listener;
    private InputBufferStateListener inputBufferStateListener;

    @Before
    public void init() {
        listener = mock(ParseTreeListener.class);
        inputBufferStateListener = mock(InputBufferStateListener.class);

        InputBufferStateListener inputBufferStateListener1 = new InputBufferStateListener() {
            @Override
            public void visitNextChar(int position, char c) {
                System.out.printf("visitNextChar => %d - %s\r\n", position, c);
            }

            @Override
            public void visitExtract(char[] chars, int start, int end) {
                System.out.printf("extract => %s - (%d, %d)\r\n", new String(chars), start, end);
            }

            @Override
            public void visitReset(int position, int marker) {

            }

            @Override
            public void visitData(String event, char[] data, int numberOfCharacters, int position, int offset) {
                System.out.printf("visitData => %s : %s - (%d, %d, %d)\r\n", event, new String(data), numberOfCharacters, position, offset);
            }
        };

        // clone data array, otherwise verifications are
        // done only on the last state of the buffer
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            char[] data = (char[])args[1];
            char[] clone = Arrays.copyOf(data, data.length);
            args[1] = clone;
            //System.out.println(args[0] + " => " + new String(data) + " : " + args[2] + " / " + args[3] + " / " + args[4]);
            return null;
        }).when(inputBufferStateListener)
          .visitData(anyString(), anyObject(), anyInt(), anyInt(),anyInt());
    }

    private ParsingResult parse(Rule rule, String text, ParseTreeListener parseTreeListener,
                                ParsingProcessListener parsingProcessListener,
                                InputBufferStateListener bufferListener,
                                int bufferSize) {

        return new ParseRunner(this, () -> rule).parse(new StringReader(text),
                parseTreeListener, parsingProcessListener, bufferListener, bufferSize);
    }

    @Test // buffer larger than input data : 1. check that buffer size is not increased 2. check that consumed data are removed from buffer
    public void test1() throws Exception {
        Rule rule = node("root", sequence(ch('a'), node("child", ch('b')), optional('c')));

        int bufferSize = 10;
        ParsingResult result = parse(rule, "abc", listener, null, inputBufferStateListener, bufferSize);

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

        char[] data = new char[bufferSize];
        data[0] = 'c';
        data[1] = 'b';
        data[2] = 'c';
        inOrder.verify(inputBufferStateListener).visitData("consume", data, 1, -1, 2);
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'c'}, 2, 2);
        inOrder.verify(listener).characters(new char[]{'c'}, 2, 2);
        inOrder.verify(listener).exitNode("root");
        inOrder.verify(inputBufferStateListener).visitData("consume", data, 0, -1, 3);

        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, never()).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // test flushing with sequence / test input buffer increase

    @Test // buffer smaller than input data : 1. check that buffer size is not increased 2. check that consumed data are removed from buffer
    public void test2() throws Exception {
        Rule rule = node("root", sequence(ch('a'), zeroOrMore(node("child", ch('b'))), optional('c')));

        ParsingResult result = parse(rule, "abbbbc", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(inputBufferStateListener).visitNextChar(0, 'a');
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(inputBufferStateListener).visitNextChar(1, 'b');
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'a', 'b'}), eq(0), eq(-1), eq(2));

        inOrder.verify(inputBufferStateListener).visitNextChar(2, 'b');
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'b'}, 2, 2);
        inOrder.verify(listener).characters(new char[]{'b'}, 2, 2);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'b', 'b'}), eq(1), eq(-1), eq(3));

        inOrder.verify(inputBufferStateListener).visitNextChar(3, 'b');
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'b'}, 3, 3);
        inOrder.verify(listener).characters(new char[]{'b'}, 3, 3);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'b', 'b'}), eq(0), eq(-1), eq(4));

        inOrder.verify(inputBufferStateListener).visitNextChar(4, 'b');
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(inputBufferStateListener).visitExtract(new char[]{'b'}, 4, 4);
        inOrder.verify(listener).characters(new char[]{'b'}, 4, 4);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'c', 'c'}), eq(1), eq(-1), eq(5));

        inOrder.verify(listener).characters(new char[]{'c'}, 5, 5);
        inOrder.verify(listener).exitNode("root");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'c', 'c'}), eq(0), eq(-1), eq(6));

        verify(inputBufferStateListener, times(5)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, never()).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());

    }

    @Test // buffer smaller than input data : 1. check that buffer size is increased once 2. check that consumed data are removed regularly from buffer
    public void test3() throws Exception {
        Rule rule = node("root", sequence(ch('a'), zeroOrMore(node("child", string("xy"))), optional(ch('b'))));

        ParsingResult result = parse(rule, "axyxyb", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        verify(inputBufferStateListener, times(3)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    @Test // buffer smaller than input data : 1. check that buffer size is increased twice 2. check that data are consumed only once
    public void test4() throws Exception {
        Rule rule = node("root", sequence(ch('a'), zeroOrMore(node("child", string("xy"))), ch('b')));

        ParsingResult result = parse(rule, "axyxyb", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        verify(inputBufferStateListener, times(1)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, times(2)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // ****** test intermediate flushings when optionals are detected ****** //

    // 'any' matcher should let flush preceeding nodes
    @Test
    public void test5() throws Exception {
        Rule child1 = node("child1", ch('b'));
        Rule child2 = node("child2", any()); // optional child
        Rule rule = node("root", sequence(ch('a'), child1, child2));

        ParsingResult result = parse(rule, "abx", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child1");
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child1");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'a', 'b'}), eq(0), eq(-1), eq(2));
        inOrder.verify(listener).enterNode("child2");
        inOrder.verify(listener).characters(new char[]{'x'}, 2, 2);
        inOrder.verify(listener).exitNode("child2");
        inOrder.verify(listener).exitNode("root");

        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, never()).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // 'empty' matcher should let flush preceeding nodes
    @Test
    public void test6() throws Exception {
        Rule child1 = node("child1", ch('b'));
        Rule child2 = node("child2", empty()); // optional child
        Rule rule = node("root", sequence(ch('a'), child1, child2));

        ParsingResult result = parse(rule, "ab", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child1");
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child1");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'a', 'b'}), eq(0), eq(-1), eq(2));
        inOrder.verify(listener).enterNode("child2");
        inOrder.verify(listener).exitNode("child2");
        inOrder.verify(listener).exitNode("root");

        verify(inputBufferStateListener, times(1)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, never()).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // 'optional' matcher should let flush preceeding nodes
    @Test
    public void test7() throws Exception {
        Rule child1 = node("child1", ch('b'));
        Rule child2 = node("child2", optional('x')); // optional child
        Rule rule = node("root", sequence(ch('a'), child1, child2));

        ParsingResult result = parse(rule, "abx", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child1");
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child1");
        inOrder.verify(inputBufferStateListener)
                .visitData(eq("consume"), aryEq(new char[] {'a', 'b'}), eq(0), eq(-1), eq(2));
        inOrder.verify(listener).enterNode("child2");
        inOrder.verify(listener).characters(new char[]{'x'}, 2, 2);
        inOrder.verify(listener).exitNode("child2");
        inOrder.verify(listener).exitNode("root");

        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, never()).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // 'zero or more' matcher should let flush preceeding nodes
    @Test
    public void test8() throws Exception {
        Rule child1 = node("child1", ch('b'));
        Rule child2 = node("child2", zeroOrMore(ch('x'))); // optional child
        Rule rule = node("root", sequence(ch('a'), child1, child2));

        ParsingResult result = parse(rule, "abx", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child1");
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child1");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'a', 'b'}), eq(0), eq(-1), eq(2));
        inOrder.verify(listener).enterNode("child2");
        inOrder.verify(listener).characters(new char[]{'x'}, 2, 2);
        inOrder.verify(listener).exitNode("child2");
        inOrder.verify(listener).exitNode("root");

        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, never()).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // 'named' matcher should let flush preceeding nodes if children are optional
    @Test
    public void test9() throws Exception {
        Rule child1 = node("child1", ch('b'));
        Rule child2 = node("child2", named("child3", optional('x'))); // optional child
        Rule rule = node("root", sequence(ch('a'), child1, child2));

        ParsingResult result = parse(rule, "abx", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child1");
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child1");
        inOrder.verify(inputBufferStateListener)
                .visitData(eq("consume"), aryEq(new char[] {'a', 'b'}), eq(0), eq(-1), eq(2));
        inOrder.verify(listener).enterNode("child2");
        inOrder.verify(listener).characters(new char[]{'x'}, 2, 2);
        inOrder.verify(listener).exitNode("child2");
        inOrder.verify(listener).exitNode("root");

        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, never()).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // 'node' matcher should let flush preceeding nodes if children are optional
    @Test
    public void test10() throws Exception {
        Rule child1 = node("child1", ch('b'));
        Rule child2 = node("child2", node("child3", optional('x'))); // optional child
        Rule rule = node("root", sequence(ch('a'), child1, child2));

        ParsingResult result = parse(rule, "abx", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child1");
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child1");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'a', 'b'}), eq(0), eq(-1), eq(2));
        inOrder.verify(listener).enterNode("child2");
        inOrder.verify(listener).characters(new char[]{'x'}, 2, 2);
        inOrder.verify(listener).exitNode("child2");
        inOrder.verify(listener).exitNode("root");

        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, never()).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // 'sequence' matcher should let flush preceeding nodes if remaining children are optional
    @Test
    public void test11() throws Exception {
        Rule child1 = node("child1", ch('b'));
        Rule child2 = node("child2", sequence(ch('x'), optional('y'))); // optional child
        Rule rule = node("root", sequence(ch('a'), child1, child2));

        ParsingResult result = parse(rule, "abxy", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("increase"), aryEq(new char[] {'a', 'b', '\u0000', '\u0000'}), eq(2), eq(1), eq(0));
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child1");
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child1");
        inOrder.verify(listener).enterNode("child2");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'x', 'y', 'x', 'y'}), eq(2), eq(0), eq(2));
        inOrder.verify(listener).characters(new char[]{'x', 'y'}, 2, 3);
        inOrder.verify(listener).exitNode("child2");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'x', 'y', 'x', 'y'}), eq(0), eq(-1), eq(4));
        inOrder.verify(listener).exitNode("root");
        //inOrder.verify(inputBufferStateListener)
        //       .visitData(eq("consume"), aryEq(new char[] {'x', 'y', 'x', 'y'}), eq(0), eq(-1), eq(4));

        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, times(1)).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // 'spying' matcher should let flush preceeding nodes if children are optional
    @Test
    public void test12() throws Exception {
        Rule child1 = node("child1", ch('b'));
        Rule child2 = node("child2", () -> new SpyingMatcher(new OptionalMatcher(ch('x')))); // optional child
        Rule rule = node("root", sequence(ch('a'), child1, child2));

        ParsingResult result = parse(rule, "abx", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child1");
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child1");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'a', 'b'}), eq(0), eq(-1), eq(2));
        inOrder.verify(listener).enterNode("child2");
        inOrder.verify(listener).characters(new char[]{'x'}, 2, 2);
        inOrder.verify(listener).exitNode("child2");
        inOrder.verify(listener).exitNode("root");

        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, never()).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }

    // 'wrapper' matcher should let flush preceeding nodes if children are optional
    @Test
    public void test13() throws Exception {
        Rule child1 = node("child1", ch('b'));
        Rule child2 = node("child2", named("child3", wrap(empty(), optional('x')))); // optional child
        Rule rule = node("root", sequence(ch('a'), child1, child2));

        ParsingResult result = parse(rule, "abx", listener, null, inputBufferStateListener, 2);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener, inputBufferStateListener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[]{'a'}, 0, 0);
        inOrder.verify(listener).enterNode("child1");
        inOrder.verify(listener).characters(new char[]{'b'}, 1, 1);
        inOrder.verify(listener).exitNode("child1");
        inOrder.verify(inputBufferStateListener)
               .visitData(eq("consume"), aryEq(new char[] {'a', 'b'}), eq(0), eq(-1), eq(2));
        inOrder.verify(listener).enterNode("child2");
        inOrder.verify(listener).characters(new char[]{'x'}, 2, 2);
        inOrder.verify(listener).exitNode("child2");
        inOrder.verify(listener).exitNode("root");

        verify(inputBufferStateListener, times(2)).visitData(eq("consume"), anyObject(), anyInt(), anyInt(), anyInt());
        verify(inputBufferStateListener, never()).visitData(eq("increase"), anyObject(), anyInt(), anyInt(), anyInt());
    }


    // ****** test no intermediate flushing occurs when no optionals are detected ****** //

    // 'any of' matcher should not let flush preceeding nodes

    // 'any of string' matcher should not let flush preceeding nodes

    // 'char range' matcher should not let flush preceeding nodes

    // 'none of' matcher should not let flush preceeding nodes

    // 'one or more' matcher should not let flush preceeding nodes

    // 'test' matcher should not let flush preceeding nodes

    // 'test not' matcher should not let flush preceeding nodes

}