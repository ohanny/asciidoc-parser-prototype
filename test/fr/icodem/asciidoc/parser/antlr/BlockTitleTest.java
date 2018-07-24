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
                "(document (content (blockTitle . (blockTitleValue A   t i t l e) <EOI>)))"
            },
            {
                /* message */
                "block  title ended by new line",

                /* input */
                ".A title\n",

                /* expected */
                "(document (content (blockTitle . (blockTitleValue A   t i t l e) \\n)) (bl <EOI>))"
            },
            {
                /* message */
                "title attached to a paragraph",

                /* input */
                ".Fruits\n" +
                "Strawberry and banana",

                /* expected */
                "(document (content (blockTitle . (blockTitleValue F r u i t s) \\n) (block (paragraph S t r a w b e r r y   a n d   b a n a n a <EOI>))))"
            },
            {
                /* message */
                "single letter title",

                /* input */
                ".a",

                /* expected */
                "(document (content (blockTitle . (blockTitleValue a) <EOI>)))"
            },
            {
                /* message */
                "single letter title ended by new line",

                /* input */
                ".a\n",

                /* expected */
                "(document (content (blockTitle . (blockTitleValue a) \\n)) (bl <EOI>))"
            },
            {
                /* message */
                "single letter title attached to single letter paragraph",

                /* input */
                ".a\n" +
                "b",

                /* expected */
                "(document (content (blockTitle . (blockTitleValue a) \\n) (block (paragraph b <EOI>))))"
            },
            {
                /* message */
                "single letter title attached to single letter paragraph ended by new line",

                /* input */
                ".a\n" +
                "b\n",

                /* expected */
                "(document (content (blockTitle . (blockTitleValue a) \\n) (block (paragraph b) (nl \\n))) (bl <EOI>))"
            },
            {
                /* message */
                "block title ended by dot characters",

                /* input */
                ".Fruits...\n" +
                "Kiwi and apple\n",

                /* expected */
                "(document (content (blockTitle . (blockTitleValue F r u i t s . . .) \\n) (block (paragraph K i w i   a n d   a p p l e) (nl \\n))) (bl <EOI>))"
            },
            {
                /* message */
                "block title before listing",

                /* input */
                ".Some code\n" +
                "----\n" +
                "int i = 0;\n" +
                "----\n",

                /* expected */
                "(document (content (blockTitle . (blockTitleValue S o m e   c o d e) \\n) (block (listingBlock (listingBlockDelimiter - - - - \\n) i n t   i   =   0 ; \\n (listingBlockDelimiter - - - - \\n)))) (bl <EOI>))"
            },
            {
                /* message */
                "block title before list",

                /* input */
                ".Some fruits\n" +
                "* Apple\n" +
                "* Kiwi\n",

                /* expected */
                "(document (content (blockTitle . (blockTitleValue S o m e   f r u i t s) \\n) (block (list (listItem *   (listItemValue A p p l e) \\n) (listItem *   (listItemValue K i w i) \\n)))) (bl <EOI>))"
            }
        });

    }

}
