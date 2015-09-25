package fr.icodem.asciidoc.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class UnorderedListTest extends GrammarTest {

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
                "* Orange",

                /* expected */
                "(document (block (unorderedList (listItem *   (listItemValue O r a n g e) <EOF>))))"
            },{
                /* message */
                "list with three items",

                /* input */
                "* Lemon\n" +
                "* Cherry\n" +
                "* Mandarine",

                /* expected */
                "(document (block (unorderedList (listItem *   (listItemValue L e m o n) \\n) (listItem *   (listItemValue C h e r r y) \\n) (listItem *   (listItemValue M a n d a r i n e) <EOF>))))"
            },
            {
                /* message */
                "list with three items, ended with new line",

                /* input */
                "* Lemon\n" +
                "* Cherry\n" +
                "* Mandarine\n",

                /* expected */
                "(document (block (unorderedList (listItem *   (listItemValue L e m o n) \\n) (listItem *   (listItemValue C h e r r y) \\n) (listItem *   (listItemValue M a n d a r i n e) \\n))))"
            },
            {
                /* message */
                "list items separated with a blank line",

                /* input */
                "* Lemon\n" +
                "\n" +
                "* Cherry\n" +
                "* Mandarine\n",

                /* expected */
                "(document (block (unorderedList (listItem *   (listItemValue L e m o n) \\n) (bl \\n) (listItem *   (listItemValue C h e r r y) \\n) (listItem *   (listItemValue M a n d a r i n e) \\n))))"
            },
            {
                /* message */
                "list items separated by a blank line with spaces and tabs",

                /* input */
                "* Lemon\n" +
                "  \t \t \n" +
                "* Cherry\n" +
                "* Mandarine\n",

                /* expected */
                "(document (block (unorderedList (listItem *   (listItemValue L e m o n) \\n) (bl     \\t   \\t   \\n) (listItem *   (listItemValue C h e r r y) \\n) (listItem *   (listItemValue M a n d a r i n e) \\n))))"
            },
            {
                /* message */
                "two lists separated by a comment",

                /* input */
                "* Lemon\n" +
                "* Cherry\n" +
                "\n" +
                "//^\n" +
                "\n" +
                "* Almond\n" +
                "* Walnut",

                /* expected */
                "(document (block (unorderedList (listItem *   (listItemValue L e m o n) \\n) (listItem *   (listItemValue C h e r r y) \\n))) (nl \\n) (block (singleComment / / ^ \\n)) (nl \\n) (block (unorderedList (listItem *   (listItemValue A l m o n d) \\n) (listItem *   (listItemValue W a l n u t) <EOF>))))"
            }
        });

    }

}
