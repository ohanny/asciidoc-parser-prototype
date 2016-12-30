package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HorizontalRuleTest extends GrammarTest {

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
                "horizontal rule",

                /* input */
                "'''",

                /* expected */
                "(document (content (horizontalRule ' ' ' <EOI>)))"
            },
            {
                /* message */
                "horizontal rule ended by new line",

                /* input */
                "'''\n",

                /* expected */
                "(document (content (horizontalRule ' ' ' \\n)) (bl <EOI>))"
            },
            {
                /* message */
                "horizontal rule between blocks",

                /* input */
                "Block above\n" +
                "\n" +
                "'''\n" +
                "\n" +
                "Block below",

                /* expected */
                "(document (content (block (paragraph B l o c k   a b o v e) (nl \\n)) (bl \\n) (horizontalRule ' ' ' \\n) (bl \\n) (block (paragraph B l o c k   b e l o w <EOI>))))"
            }
        });

    }

}
