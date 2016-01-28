package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParserTest extends BaseParser {

    private ParseTreeListener listener;

    private ParseTreeListener listener2 = new ParseTreeListener() {
        @Override
        public void characters(char[] characters, int startIndex, int endIndex) {
            System.out.println("\tchars => [" + startIndex + "," + endIndex + "] " + new String(characters));
        }

        @Override
        public void enterNode(String nodeName) {
            System.out.println("ENTER => " + nodeName);
        }

        @Override
        public void exitNode(String nodeName) {
            System.out.println("EXIT => " + nodeName);
        }
    };

    @Before
    public void init() {
        listener = mock(ParseTreeListener.class);
    }

    @Test
    public void twoCharsInSequenceWithCorrectInput() throws Exception {
        Rule rule = sequence(ch('a'), ch('b'));
        InputBuffer input = new InputBuffer("ab");

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
    }

    @Test
    public void twoCharsInSequenceWithIncorrectInput() throws Exception {
        Rule rule = sequence(ch('a'), ch('b'));
        InputBuffer input = new InputBuffer("zz");

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input);

        boolean matched = matcher.match(context);

        Assert.assertFalse("Did not match", matched);
    }

    @Test
    public void test1() throws Exception {
        Rule rule = node("root", sequence(ch('a'), ch('b')));
        InputBuffer input = new InputBuffer("ab");

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input, listener);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test2() throws Exception {

        Rule rule1 = node("child", sequence(ch('c'), ch('d')));
        Rule rule2 = node("root", sequence(ch('a'), ch('b'), rule1));
        InputBuffer input = new InputBuffer("abcd");

        Matcher matcher = rule2.getMatcher();
        MatcherContext context = new MatcherContext(input, listener);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(listener).characters(new char[] {'c', 'd'}, 2, 3);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test3() throws Exception {

        Rule rule = node("root", zeroOrMore(ch('a')));
        InputBuffer input = new InputBuffer("ab");

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input, listener);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test4() throws Exception {

        Rule rule = node("root", sequence(optional(ch('a')), ch('b')));
        InputBuffer input = new InputBuffer("ab");

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input, listener);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test5() throws Exception {

        Rule rule = node("root", sequence(optional(ch('a')), ch('b')));
        InputBuffer input = new InputBuffer("b");

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input, listener);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'b'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test7() throws Exception {

        Rule rule = node("expression", sequence(ch('a'), zeroOrMore(proxy("expression")), ch('b')));
        InputBuffer input = new InputBuffer("aabb");

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input, listener);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("expression");
        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).enterNode("expression");
        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 1, 2);
        inOrder.verify(listener).exitNode("expression");
        inOrder.verify(listener).characters(new char[] {'b'}, 3, 3);
        inOrder.verify(listener).exitNode("expression");

    }

    @Test
    public void test8() throws Exception {

        Rule rule = node("expression", sequence(ch('a'), zeroOrMore(proxy("expression")), ch('b')));
        InputBuffer input = new InputBuffer("aababb");

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input, listener);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("expression");
        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).enterNode("expression");
        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 1, 2);
        inOrder.verify(listener).exitNode("expression");
        inOrder.verify(listener).enterNode("expression");
        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 3, 4);
        inOrder.verify(listener).exitNode("expression");
        inOrder.verify(listener).characters(new char[] {'b'}, 5, 5);
        inOrder.verify(listener).exitNode("expression");

    }

    @Test
    public void test9() throws Exception {

        Rule rule = node("expression", sequence(ch('a'), zeroOrMore(proxy("expression")), ch('b')));
        InputBuffer input = new InputBuffer("aababb");

        ToStringTreeBuilder treeBuilder = new ToStringTreeBuilder();

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input, treeBuilder);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        assertEquals("", "(expression a (expression a b) (expression a b) b)", treeBuilder.getStringTree());
    }

    @Test
    public void test10() throws Exception {

        Rule rule = node("root", firstOf(ch('a'), ch('b')));
        InputBuffer input = new InputBuffer("a");

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input, listener);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test11() throws Exception {

        Rule rule = node("root", firstOf(ch('a'), ch('b')));
        InputBuffer input = new InputBuffer("b");

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input, listener);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'b'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

}
