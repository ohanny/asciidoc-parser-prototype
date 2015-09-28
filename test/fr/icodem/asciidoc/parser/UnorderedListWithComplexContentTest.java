package fr.icodem.asciidoc.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class UnorderedListWithComplexContentTest extends GrammarTest {

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
                "one list item with two lines",

                /* input */
                "* The earth, the sea\n" +
                " and the sun",

                /* expected */
                "(document (block (unorderedList (listItem *   (listItemValue T h e   e a r t h ,   t h e   s e a \\n   a n d   t h e   s u n) <EOF>))))"
            },
            {
                /* message */
                "second line of list item is not a comment",

                /* input */
                "* The earth, the sea\n" +
                "// and the sun",

                /* expected */
                "(document (block (unorderedList (listItem *   (listItemValue T h e   e a r t h ,   t h e   s e a \\n / /   a n d   t h e   s u n) <EOF>))))"
            },
            {
                /* message */
                "three list items with two lines",

                /* input */
                "* The earth, the sea\n" +
                " and the sun\n" +
                "\n" +
                "* Kiwi, kaki\n" +
                " and kiwai\n" +
                "\n" +
                "* Cabbage, leek\n" +
                " and carrot",

                /* expected */
                "(document (block (unorderedList (listItem *   (listItemValue T h e   e a r t h ,   t h e   s e a \\n   a n d   t h e   s u n) \\n) (bl \\n) (listItem *   (listItemValue K i w i ,   k a k i \\n   a n d   k i w a i) \\n) (bl \\n) (listItem *   (listItemValue C a b b a g e ,   l e e k \\n   a n d   c a r r o t) <EOF>))))"
            }
        });

    }

}
