package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LiteralBlockTest extends GrammarTest {

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
                "a simple literal block",

                /* input */
                "....\n" +
                "Some text\n" +
                "....",

                /* expected */
                "(document (content (preamble (block (literalBlock (literalBlockDelimiter . . . . \\n) S o m e   t e x t \\n (literalBlockDelimiter . . . . <EOI>))))))"
            },
            {
                /* message */
                "a simple literal block ending with new line",

                /* input */
                "....\n" +
                "Some text\n" +
                "....\n",

                /* expected */
                "(document (content (preamble (block (literalBlock (literalBlockDelimiter . . . . \\n) S o m e   t e x t \\n (literalBlockDelimiter . . . . \\n))))) (bl <EOI>))"
            }
        });

    }

}
