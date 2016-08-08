package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.listeners.ToStringTreeBuilder;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.spyingRulesFactory;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParserTest extends BaseRules {

    private ParseTreeListener listener;

    private ArgumentCaptor<NodeContext> ac;

    private ParsingResult parse(Rule rule, String text) {
        return new ParseRunner(this, () -> rule).parse(text);
    }

    private ParsingResult parse(Rule rule, String text, ParseTreeListener listener) {
        return new ParseRunner(this, () -> rule).parse(text, listener);
    }

    @Before
    public void init() {
        listener = mock(ParseTreeListener.class);
        ac = ArgumentCaptor.forClass(NodeContext.class);
        //useSpyingRulesFactory();
        useFactory(spyingRulesFactory());
    }

    @Test
    public void twoCharsInSequenceWithCorrectInput() throws Exception {
        Rule rule = sequence(ch('a'), ch('b'));

        ParsingResult result = parse(rule, "ab");

        assertTrue("Did not match", result.matched);
    }

    @Test
    public void twoCharsInSequenceWithIncorrectInput() throws Exception {
        Rule rule = sequence(ch('a'), ch('b'));

        ParsingResult result = parse(rule, "zz");

        assertFalse("Did not match", result.matched);
    }

    @Test
    public void test1() throws Exception {
        Rule rule = node("root", sequence(ch('a'), ch('b')));
        ParsingResult result = parse(rule, "ab", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);
        inOrder.verify(listener).exitNode("root");
    }

    @Test
    public void test2() throws Exception {

        Rule rule1 = node("child", sequence(ch('c'), ch('d')));
        Rule rule2 = node("root", sequence(ch('a'), ch('b'), rule1));

        ParsingResult result = parse(rule2, "abcd", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "child", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'c', 'd'}, 2, 3);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test3() throws Exception {

        Rule rule = node("root", zeroOrMore(ch('a')));

        ParsingResult result = parse(rule, "ab", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test4() throws Exception {

        Rule rule = node("root", sequence(optional(ch('a')), ch('b')));

        ParsingResult result = parse(rule, "ab", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test5() throws Exception {

        Rule rule = node("root", sequence(optional(ch('a')), ch('b')));

        ParsingResult result = parse(rule, "b", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'b'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test7() throws Exception {

        Rule rule = node("expression", sequence(ch('a'), zeroOrMore(proxy("expression")), ch('b')));

        ParsingResult result = parse(rule, "aabb", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "expression", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "expression", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 1, 2);
        inOrder.verify(listener).exitNode("expression");
        inOrder.verify(listener).characters(new char[] {'b'}, 3, 3);
        inOrder.verify(listener).exitNode("expression");

    }

    @Test
    public void test8() throws Exception {

        Rule rule = node("expression", sequence(ch('a'), zeroOrMore(proxy("expression")), ch('b')));

        ParsingResult result = parse(rule, "aababb", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "expression", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "expression", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 1, 2);
        inOrder.verify(listener).exitNode("expression");

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "expression", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 3, 4);
        inOrder.verify(listener).exitNode("expression");
        inOrder.verify(listener).characters(new char[] {'b'}, 5, 5);
        inOrder.verify(listener).exitNode("expression");

    }

    @Test
    public void test9() throws Exception {

        Rule rule = node("expression", sequence(ch('a'), zeroOrMore(proxy("expression")), ch('b')));

        ToStringTreeBuilder treeBuilder = new ToStringTreeBuilder();

        ParsingResult result = parse(rule, "aababb", treeBuilder);

        assertTrue("Did not match", result.matched);
        assertEquals("", "(expression a (expression a b) (expression a b) b)", treeBuilder.getStringTree());
    }

    @Test
    public void test10() throws Exception {

        Rule rule = node("root", firstOf(ch('a'), ch('b')));

        ParsingResult result = parse(rule, "a", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test11() throws Exception {

        Rule rule = node("root", firstOf(ch('a'), ch('b')));

        ParsingResult result = parse(rule, "b", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'b'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test12() throws Exception {

        Rule rule = node("root", oneOrMore(ch('a')));

        ParsingResult result = parse(rule, "a", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test13() throws Exception {

        Rule rule = node("root", oneOrMore(ch('a')));

        ParsingResult result = parse(rule, "aaa", listener);

        assertTrue("Did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'a', 'a'}, 0, 2);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test14() throws Exception {

        Rule rule = node("root", oneOrMore(ch('a')));

        ParsingResult result = parse(rule, "b", listener);

        assertFalse("Rule matched", result.matched);
    }

    @Test
    public void test15() throws Exception {

        Rule rule = node("root", firstOf(sequence(ch('a'), ch('b')), sequence(ch('c'), ch('d'))));

        ParsingResult result = parse(rule, "ab", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test16() throws Exception {

        Rule rule = node("root", firstOf(sequence(ch('a'), ch('b')), sequence(ch('c'), ch('d'))));

        ParsingResult result = parse(rule, "cd", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'c', 'd'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test17() throws Exception {

        Rule rule1 = firstOf(sequence(ch('a'), ch('b')), oneOrMore(ch('c')));
        Rule rule2 = firstOf(sequence(ch('c'), ch('d')), zeroOrMore(ch('d')));
        Rule rule = node("root", sequence(rule1, rule2));

        ParsingResult result = parse(rule, "abddd", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'b', 'd', 'd', 'd'}, 0, 4);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test18() throws Exception {

        Rule rule1 = firstOf(sequence(ch('a'), ch('b')), oneOrMore(ch('c')));
        Rule rule2 = firstOf(sequence(ch('c'), ch('d')), zeroOrMore(ch('d')));
        Rule rule = node("root", sequence(rule1, rule2));

        ParsingResult result = parse(rule, "ab", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'b'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test19() throws Exception {

        Rule rule1 = firstOf(sequence(ch('a'), ch('b')), oneOrMore(ch('c')));
        Rule rule2 = firstOf(sequence(ch('c'), ch('d')), zeroOrMore(ch('d')));
        Rule rule = node("root", sequence(rule1, rule2));

        ParsingResult result = parse(rule, "cccd", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'c', 'c', 'c', 'd'}, 0, 3);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test20() throws Exception {

        Rule rule = node("root", sequence(test(sequence(ch('a'), ch('b'))), oneOrMore(firstOf(ch('a'),ch('b'),ch('c'),ch('d')))));

        ParsingResult result = parse(rule, "abcd", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'b', 'c', 'd'}, 0, 3);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test21() throws Exception {

        Rule rule2 = sequence(ch('c'), ch('d'));
        Rule rule1 = firstOf(sequence(ch('a'), ch('b')), oneOrMore(sequence(testNot(rule2), ch('c'))));
        Rule rule = node("root", sequence(rule1, rule2));

        ParsingResult result = parse(rule, "cccd", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'c', 'c', 'c', 'd'}, 0, 3);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test22() throws Exception {

        Rule rule = charRange('a', 'f');

        ParsingResult result = parse(rule, "a");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test23() throws Exception {

        Rule rule = charRange('a', 'f');

        ParsingResult result = parse(rule, "f");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test24() throws Exception {

        Rule rule = charRange('a', 'f');

        ParsingResult result = parse(rule, "g");

        assertFalse("Rule should not matched", result.matched);
    }

    @Test
    public void test25() throws Exception {

        Rule rule = node("root", sequence(charRange('a', 'c'), ch('z')));

        ParsingResult result = parse(rule, "az", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'z'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test26() throws Exception {

        Rule rule = node("root", sequence(charRange('a', 'c'), ch('z')));

        ParsingResult result = parse(rule, "bz", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'b', 'z'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test27() throws Exception {

        Rule rule = node("root", sequence(charRange('a', 'c'), ch('z')));

        ParsingResult result = parse(rule, "kz", listener);

        assertFalse("Rule should not match", result.matched);

    }

    @Test
    public void test28() throws Exception {

        Rule rule = anyOf('a', 'x', 'i');

        ParsingResult result = parse(rule, "a");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test29() throws Exception {

        Rule rule = anyOf('a', 'x', 'i');

        ParsingResult result = parse(rule, "i");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test30() throws Exception {

        Rule rule = anyOf('a', 'x', 'i');

        ParsingResult result = parse(rule, "k");

        assertFalse("Rule should not match", result.matched);
    }

    @Test
    public void test31() throws Exception {

        Rule rule = node("root", sequence(anyOf('y', 'm'), ch('z')));

        ParsingResult result = parse(rule, "yz", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", ac.getValue().getNodeName(), "root");

        inOrder.verify(listener).characters(new char[] {'y', 'z'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test32() throws Exception {

        Rule rule = node("root", sequence(anyOf('y', 'm'), ch('z')));

        ParsingResult result = parse(rule, "mz", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'m', 'z'}, 0, 1);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test33() throws Exception {

        Rule rule = node("root", sequence(anyOf('y', 'm'), ch('z')));

        ParsingResult result = parse(rule, "kz");

        assertFalse("Rule should not match", result.matched);

    }

    @Test
    public void test34() throws Exception {

        Rule rule = string("abc");

        ParsingResult result = parse(rule, "abc");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test35() throws Exception {

        Rule rule = string("abc");

        ParsingResult result = parse(rule, "ab");

        assertFalse("Rule should not match", result.matched);
    }

    @Test
    public void test36() throws Exception {

        Rule rule = node("root", sequence(string("abc"), ch('z')));

        ParsingResult result = parse(rule, "abcz", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'a', 'b', 'c', 'z'}, 0, 3);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test37() throws Exception {

        Rule rule = anyOfString("abc", "def");

        ParsingResult result = parse(rule, "abc");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test38() throws Exception {

        Rule rule = anyOfString("abc", "def");

        ParsingResult result = parse(rule, "def");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test39() throws Exception {

        Rule rule = anyOfString("abc", "def");

        ParsingResult result = parse(rule, "gh");

        assertFalse("Rule should not match", result.matched);
    }

    @Test
    public void test40() throws Exception {

        Rule rule = node("root", sequence(anyOfString("abc", "def"), ch('z')));

        ParsingResult result = parse(rule, "defz", listener);

        assertTrue("Rule did not match", result.matched);
        InOrder inOrder = inOrder(listener);

        inOrder.verify(listener).enterNode(ac.capture());
        assertEquals("Node name incorrect", "root", ac.getValue().getNodeName());

        inOrder.verify(listener).characters(new char[] {'d', 'e', 'f', 'z'}, 0, 3);
        inOrder.verify(listener).exitNode("root");

    }

    @Test
    public void test41() throws Exception {

        Rule rule = noneOf("abc");

        ParsingResult result = parse(rule, "z");

        assertTrue("Rule did not match", result.matched);
    }

    @Test
    public void test42() throws Exception {

        Rule rule = noneOf("abc");

        ParsingResult result = parse(rule, "b");

        assertFalse("Rule should not match", result.matched);
    }


}
