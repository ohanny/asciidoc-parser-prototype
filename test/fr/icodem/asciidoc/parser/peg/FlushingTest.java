package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlushingTest extends BaseParser {
    private ParseTreeListener listener;
    private InputBufferVisitor visitor;

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
        visitor = mock(InputBufferVisitor.class);
    }

    @Test
    public void test1() throws Exception {
        Rule rule = node("root", sequence(node("child", ch('a')), ch('b')));
        InputBuffer input = new InputBuffer("ab", visitor);

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input, listener);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        InOrder inOrder = inOrder(listener, visitor);
        inOrder.verify(visitor).visitNextChar(0, 'a');
        inOrder.verify(visitor).visitNextChar(1, 'b');
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(visitor).visitExtract(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(listener).characters(new char[] {'b'}, 1, 1);
        inOrder.verify(listener).exitNode("root");
    }

    @Test
    public void test2() throws Exception {
        Rule rule = node("root", sequence(node("child", ch('a')), optional(ch('b'))));
        InputBuffer input = new InputBuffer("ab", visitor);

        Matcher matcher = rule.getMatcher();
        MatcherContext context = new MatcherContext(input, listener);

        boolean matched = matcher.match(context);

        assertTrue("Did not match", matched);
        InOrder inOrder = inOrder(listener, visitor);
        inOrder.verify(visitor).visitNextChar(0, 'a');
        inOrder.verify(listener).enterNode("root");
        inOrder.verify(listener).enterNode("child");
        inOrder.verify(visitor).visitExtract(new char[] {'a'}, 0, 0);
        inOrder.verify(listener).exitNode("child");
        inOrder.verify(visitor).visitNextChar(1, 'b');
        inOrder.verify(listener).characters(new char[] {'b'}, 1, 1);
        inOrder.verify(listener).exitNode("root");
    }
}
