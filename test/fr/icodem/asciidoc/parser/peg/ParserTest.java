package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.listeners.ToStringAnalysisBuilder;
import fr.icodem.asciidoc.parser.peg.listeners.ToStringTreeBuilder;
import fr.icodem.asciidoc.parser.peg.matchers.Matcher;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
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

        ParsingResult result = new ParseRunner(rule).parse("ab");

        assertTrue("Did not match", result.matched);
    }

    @Test
    public void twoCharsInSequenceWithIncorrectInput() throws Exception {
        Rule rule = sequence(ch('a'), ch('b'));

        ParsingResult result = new ParseRunner(rule).parse("zz");

        assertFalse("Did not match", result.matched);
    }

    @Test
    public void test1() throws Exception {
        Rule rule = node("root", sequence(ch('a'), ch('b')));
        ParsingResult result = new ParseRunner(rule).parse("ab", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);
        inOrder.verify(listener).exitNode("root");
    }

    @Test
    public void test2() throws Exception {

        Rule rule1 = node("child", sequence(ch('c'), ch('d')));
        Rule rule2 = node("root", sequence(ch('a'), ch('b'), rule1));

        ParsingResult result = new ParseRunner(rule2).parse("abcd", listener);

        assertTrue("Did not match", result.matched);
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

        ParsingResult result = new ParseRunner(rule).parse("ab", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test4() throws Exception {

        Rule rule = node("root", sequence(optional(ch('a')), ch('b')));

        ParsingResult result = new ParseRunner(rule).parse("ab", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test5() throws Exception {

        Rule rule = node("root", sequence(optional(ch('a')), ch('b')));

        ParsingResult result = new ParseRunner(rule).parse("b", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'b'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test7() throws Exception {

        Rule rule = node("expression", sequence(ch('a'), zeroOrMore(proxy("expression")), ch('b')));

        ParsingResult result = new ParseRunner(rule).parse("aabb", listener);

        assertTrue("Did not match", result.matched);
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

        ParsingResult result = new ParseRunner(rule).parse("aababb", listener);

        assertTrue("Did not match", result.matched);
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

        ToStringTreeBuilder treeBuilder = new ToStringTreeBuilder();

        ParsingResult result = new ParseRunner(rule).parse("aababb", treeBuilder);

        assertTrue("Did not match", result.matched);
        assertEquals("", "(expression a (expression a b) (expression a b) b)", treeBuilder.getStringTree());
    }

    @Test
    public void test10() throws Exception {

        Rule rule = node("root", firstOf(ch('a'), ch('b')));

        ParsingResult result = new ParseRunner(rule).parse("a", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test11() throws Exception {

        Rule rule = node("root", firstOf(ch('a'), ch('b')));

        ParsingResult result = new ParseRunner(rule).parse("b", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'b'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test12() throws Exception {

        Rule rule = node("root", oneOrMore(ch('a')));

        ParsingResult result = new ParseRunner(rule).parse("a", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test13() throws Exception {

        Rule rule = node("root", oneOrMore(ch('a')));

        ParsingResult result = new ParseRunner(rule).parse("aaa", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'a', 'a'}, 0, 2);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test14() throws Exception {

        Rule rule = node("root", oneOrMore(ch('a')));

        ParsingResult result = new ParseRunner(rule).parse("b", listener);

        assertFalse("Rule matched", result.matched);
    }

    @Test
    public void test15() throws Exception {

        Rule rule = node("root", firstOf(sequence(ch('a'), ch('b')), sequence(ch('c'), ch('d'))));

        ParsingResult result = new ParseRunner(rule).parse("ab", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test16() throws Exception {

        Rule rule = node("root", firstOf(sequence(ch('a'), ch('b')), sequence(ch('c'), ch('d'))));

        ParsingResult result = new ParseRunner(rule).parse("cd", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'c', 'd'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test17() throws Exception {

        Rule rule1 = firstOf(sequence(ch('a'), ch('b')), oneOrMore(ch('c')));
        Rule rule2 = firstOf(sequence(ch('c'), ch('d')), zeroOrMore(ch('d')));
        Rule rule = node("root", sequence(rule1, rule2));

        ParsingResult result = new ParseRunner(rule).parse("abddd", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'b', 'd', 'd', 'd'}, 0, 4);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test18() throws Exception {

        Rule rule1 = firstOf(sequence(ch('a'), ch('b')), oneOrMore(ch('c')));
        Rule rule2 = firstOf(sequence(ch('c'), ch('d')), zeroOrMore(ch('d')));
        Rule rule = node("root", sequence(rule1, rule2));

        ParsingResult result = new ParseRunner(rule).parse("ab", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test19() throws Exception {

        Rule rule1 = firstOf(sequence(ch('a'), ch('b')), oneOrMore(ch('c')));
        Rule rule2 = firstOf(sequence(ch('c'), ch('d')), zeroOrMore(ch('d')));
        Rule rule = node("root", sequence(rule1, rule2));

        ParsingResult result = new ParseRunner(rule).parse("cccd", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'c', 'c', 'c', 'd'}, 0, 3);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test20() throws Exception {

        Rule rule = node("root", sequence(test(sequence(ch('a'), ch('b'))), oneOrMore(firstOf(ch('a'),ch('b'),ch('c'),ch('d')))));

        ParsingResult result = new ParseRunner(rule).parse("abcd", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'b', 'c', 'd'}, 0, 3);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test21() throws Exception {

        Rule rule2 = sequence(ch('c'), ch('d'));
        Rule rule1 = firstOf(sequence(ch('a'), ch('b')), oneOrMore(sequence(testNot(rule2), ch('c'))));
        Rule rule = node("root", sequence(rule1, rule2));

        ParsingResult result = new ParseRunner(rule).parse("cccd", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'c', 'c', 'c', 'd'}, 0, 3);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test22() throws Exception {

        Rule rule = charInRange('a', 'f');

        ParsingResult result = new ParseRunner(rule).parse("a");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test23() throws Exception {

        Rule rule = charInRange('a', 'f');

        ParsingResult result = new ParseRunner(rule).parse("f");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test24() throws Exception {

        Rule rule = charInRange('a', 'f');

        ParsingResult result = new ParseRunner(rule).parse("g");

        assertFalse("Rule should not matched", result.matched);
    }

    @Test
    public void test25() throws Exception {

        Rule rule = node("root", sequence(charInRange('a', 'c'), ch('z')));

        ParsingResult result = new ParseRunner(rule).parse("az", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'z'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test26() throws Exception {

        Rule rule = node("root", sequence(charInRange('a', 'c'), ch('z')));

        ParsingResult result = new ParseRunner(rule).parse("bz", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'b', 'z'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test27() throws Exception {

        Rule rule = node("root", sequence(charInRange('a', 'c'), ch('z')));

        ParsingResult result = new ParseRunner(rule).parse("kz", listener);

        assertFalse("Rule should not match", result.matched);

    }

    @Test
    public void test28() throws Exception {

        Rule rule = charInSet('a', 'x', 'i');

        ParsingResult result = new ParseRunner(rule).parse("a");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test29() throws Exception {

        Rule rule = charInSet('a', 'x', 'i');

        ParsingResult result = new ParseRunner(rule).parse("i");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test30() throws Exception {

        Rule rule = charInSet('a', 'x', 'i');

        ParsingResult result = new ParseRunner(rule).parse("k");

        assertFalse("Rule should not match", result.matched);
    }

    @Test
    public void test31() throws Exception {

        Rule rule = node("root", sequence(charInSet('y', 'm'), ch('z')));

        ParsingResult result = new ParseRunner(rule).parse("yz", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'y', 'z'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test32() throws Exception {

        Rule rule = node("root", sequence(charInSet('y', 'm'), ch('z')));

        ParsingResult result = new ParseRunner(rule).parse("mz", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'m', 'z'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test33() throws Exception {

        Rule rule = node("root", sequence(charInSet('y', 'm'), ch('z')));

        ParsingResult result = new ParseRunner(rule).parse("kz");

        assertFalse("Rule should not match", result.matched);

    }

    @Test
    public void test34() throws Exception {

        Rule rule = string("abc");

        ParsingResult result = new ParseRunner(rule).parse("abc");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test35() throws Exception {

        Rule rule = string("abc");

        ParsingResult result = new ParseRunner(rule).parse("ab");

        assertFalse("Rule should not match", result.matched);
    }

    @Test
    public void test36() throws Exception {

        Rule rule = node("root", sequence(string("abc"), ch('z')));

        ParsingResult result = new ParseRunner(rule).parse("abcz", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'a', 'b', 'c', 'z'}, 0, 3);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test37() throws Exception {

        Rule rule = stringInSet("abc", "def");

        ParsingResult result = new ParseRunner(rule).parse("abc");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test38() throws Exception {

        Rule rule = stringInSet("abc", "def");

        ParsingResult result = new ParseRunner(rule).parse("def");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test39() throws Exception {

        Rule rule = stringInSet("abc", "def");

        ParsingResult result = new ParseRunner(rule).parse("gh");

        assertFalse("Rule should not match", result.matched);
    }

    @Test
    public void test40() throws Exception {

        Rule rule = node("root", sequence(stringInSet("abc", "def"), ch('z')));

        ParsingResult result = new ParseRunner(rule).parse("defz", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).characters(new char[] {'d', 'e', 'f', 'z'}, 0, 3);
        inOrder.verify(listener).exitNode("root");

    }

}
