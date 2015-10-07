package fr.icodem.asciidoc.parser;

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
                "A block  title ended by EOF",

                /* input */
                ".A title",

                /* expected */
                "(document (blockTitle . (title A   t i t l e)))"
            },
            {
                /* message */
                "A block  title ended by new line",

                /* input */
                ".A title\n",

                /* expected */
                "(document (blockTitle . (title A   t i t l e) \\n))"
            },
            {
                /* message */
                "A block  above a paragraph",

                /* input */
                ".Fruits\n" +
                "Strawberry and banana",

                /* expected */
                "(document (blockTitle . (title F r u i t s) \\n) (block (paragraph S t r a w b e r r y   a n d   b a n a n a)))"
            }
        });

    }

}
