package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class OrderedListTest extends GrammarTest {

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
                "list with one item",

                /* input */
                ". Orange",

                /* expected */
                "(document (content (block (unorderedList (listItem .   (listItemValue O r a n g e) <EOF>)))))"
            },
            {
                /* message */
                "list with one item ended by new line",

                /* input */
                ". Orange\n",

                /* expected */
                "(document (content (block (unorderedList (listItem .   (listItemValue O r a n g e) \\n)))) (bl <EOF>))"
            },
            {
                /* message */
                "list with three items",

                /* input */
                ". Lemon\n" +
                ". Cherry\n" +
                ". Mandarine",

                /* expected */
                "(document (content (block (unorderedList (listItem .   (listItemValue L e m o n) \\n) (listItem .   (listItemValue C h e r r y) \\n) (listItem .   (listItemValue M a n d a r i n e) <EOF>)))))"
            },
            {
                /* message */
                "list with three items, ended with new line",

                /* input */
                ". Lemon\n" +
                ". Cherry\n" +
                ". Mandarine\n",

                /* expected */
                "(document (content (block (unorderedList (listItem .   (listItemValue L e m o n) \\n) (listItem .   (listItemValue C h e r r y) \\n) (listItem .   (listItemValue M a n d a r i n e) \\n)))) (bl <EOF>))"
            },
            {
                /* message */
                "list with a title",

                /* input */
                ".Some fruits\n" +
                ". Lemon\n" +
                ". Cherry\n" +
                ". Mandarine\n",

                /* expected */
                "(document (content (blockTitle . (title S o m e   f r u i t s) \\n) (block (unorderedList (listItem .   (listItemValue L e m o n) \\n) (listItem .   (listItemValue C h e r r y) \\n) (listItem .   (listItemValue M a n d a r i n e) \\n)))) (bl <EOF>))"
            },
            {
                /* message */
                "list items separated with a blank line",

                /* input */
                ". Lemon\n" +
                "\n" +
                ". Cherry\n" +
                ". Mandarine\n",

                /* expected */
                "(document (content (block (unorderedList (listItem .   (listItemValue L e m o n) \\n) (bl \\n) (listItem .   (listItemValue C h e r r y) \\n) (listItem .   (listItemValue M a n d a r i n e) \\n)))) (bl <EOF>))"
            },
            {
                /* message */
                "list items separated by a blank line with spaces and tabs",

                /* input */
                ". Lemon\n" +
                "  \t \t \n" +
                ". Cherry\n" +
                ". Mandarine\n",

                /* expected */
                "(document (content (block (unorderedList (listItem .   (listItemValue L e m o n) \\n) (bl     \\t   \\t   \\n) (listItem .   (listItemValue C h e r r y) \\n) (listItem .   (listItemValue M a n d a r i n e) \\n)))) (bl <EOF>))"
            },
            {
                /* message */
                "two lists separated by a comment",

                /* input */
                ". Lemon\n" +
                ". Cherry\n" +
                "\n" +
                "//^\n" +
                "\n" +
                ". Almond\n" +
                ". Walnut",

                /* expected */
                "(document (content (block (unorderedList (listItem .   (listItemValue L e m o n) \\n) (listItem .   (listItemValue C h e r r y) \\n))) (bl \\n) (block (singleComment / / ^ \\n)) (bl \\n) (block (unorderedList (listItem .   (listItemValue A l m o n d) \\n) (listItem .   (listItemValue W a l n u t) <EOF>)))))"
            },
            {
                /* message */
                "simple nested list",

                /* input */
                ". Fruits\n" +
                ".. Cherry",

                /* expected */
                "(document (content (block (unorderedList (listItem .   (listItemValue F r u i t s) \\n) (listItem . .   (listItemValue C h e r r y) <EOF>)))))"
            },
            {
                /* message */
                "nested list",

                /* input */
                ". Fruits\n" +
                ".. Cherry\n" +
                ".. Kiwi\n" +
                ". Vegetables\n" +
                ".. Cabbage\n" +
                ".. Salad\n" +
                "... Green salad\n" +
                "... Red salad",

                /* expected */
                "(document (content (block (unorderedList (listItem .   (listItemValue F r u i t s) \\n) (listItem . .   (listItemValue C h e r r y) \\n) (listItem . .   (listItemValue K i w i) \\n) (listItem .   (listItemValue V e g e t a b l e s) \\n) (listItem . .   (listItemValue C a b b a g e) \\n) (listItem . .   (listItemValue S a l a d) \\n) (listItem . . .   (listItemValue G r e e n   s a l a d) \\n) (listItem . . .   (listItemValue R e d   s a l a d) <EOF>)))))"
            }
        });

    }

}
