package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BlockTitleTest extends GrammarTest {

    @Parameter(0)
    public String message;

    @Parameter(1)
    public String input;

    @Parameter(2)
    public String expected;

    @Test
    public void test() throws Exception {
        check(message, input, expected);
    }

    @Parameters
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][]{
            {
                /* message */
                "block  title ended by EOF",

                /* input */
                ".A title",

                /* expected */
                "(document (body (blockTitle . (title A   t i t l e) <EOF>)))"
            },
            {
                /* message */
                "block  title ended by new line",

                /* input */
                ".A title\n",

                /* expected */
                "(document (body (blockTitle . (title A   t i t l e) \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "title attached to a paragraph",

                /* input */
                ".Fruits\n" +
                "Strawberry and banana",

                /* expected */
                "(document (body (blockTitle . (title F r u i t s) \\n) (block (paragraph S t r a w b e r r y   a n d   b a n a n a <EOF>))))"
            },
            {
                /* message */
                "single letter title",

                /* input */
                ".a",

                /* expected */
                "(document (body (blockTitle . (title a) <EOF>)))"
            },
            {
                /* message */
                "single letter title ended by new line",

                /* input */
                ".a\n",

                /* expected */
                "(document (body (blockTitle . (title a) \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "single letter title attached to single letter paragraph",

                /* input */
                ".a\n" +
                "b",

                /* expected */
                "(document (body (blockTitle . (title a) \\n) (block (paragraph b <EOF>))))"
            },
            {
                /* message */
                "single letter title attached to single letter paragraph ended by new line",

                /* input */
                ".a\n" +
                "b\n",

                /* expected */
                "(document (body (blockTitle . (title a) \\n) (block (paragraph b)) (nl \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "block title ended by dot characters",

                /* input */
                ".Fruits...\n" +
                "Kiwi and apple\n",

                /* expected */
                "(document (body (blockTitle . (title F r u i t s . . .) \\n) (block (paragraph K i w i   a n d   a p p l e)) (nl \\n)) (bl <EOF>))"
            }
        });

    }

}
