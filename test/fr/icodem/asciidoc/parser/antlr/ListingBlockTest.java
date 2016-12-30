package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ListingBlockTest extends GrammarTest {

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
                "a simple listing block",

                /* input */
                "----\n" +
                "int a = 10;\n" +
                "----",

                /* expected */
                "(document (content (block (listingBlock (listingBlockDelimiter - - - - \\n) i n t   a   =   1 0 ; \\n (listingBlockDelimiter - - - - <EOI>)))))"
            },
            {
                /* message */
                "a simple source block ending with new line",

                /* input */
                "----\n" +
                "int a = 10;\n" +
                "----\n",

                /* expected */
                "(document (content (block (listingBlock (listingBlockDelimiter - - - - \\n) i n t   a   =   1 0 ; \\n (listingBlockDelimiter - - - - \\n)))) (bl <EOI>))"
            },
            {
                /* message */
                "a source block containing minus sign",

                /* input */
                "----\n" +
                "int a = -10;\n" +
                "----",

                /* expected */
                "(document (content (block (listingBlock (listingBlockDelimiter - - - - \\n) i n t   a   =   - 1 0 ; \\n (listingBlockDelimiter - - - - <EOI>)))))"
            }
        });

    }

}
