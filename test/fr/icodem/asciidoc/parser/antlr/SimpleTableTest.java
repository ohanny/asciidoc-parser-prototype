package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SimpleTableTest extends GrammarTest {

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
                "simple table",

                /* input */
                "|===\n" +
                "\n" +
                "| Cell 1 | Cell 2\n" +
                "\n" +
                "| Cell 3 | Cell 4\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableCell | (tableCellContent   C e l l   1  )) (tableCell | (tableCellContent   C e l l   2 (nl \\n))) (bl \\n) (tableCell | (tableCellContent   C e l l   3  )) (tableCell | (tableCellContent   C e l l   4 (nl \\n))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            }
        });

    }

}
