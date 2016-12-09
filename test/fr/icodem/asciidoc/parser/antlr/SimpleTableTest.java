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
                "one row, cells on the same line",

                /* input */
                "|===\n" +
                "\n" +
                "|Cell 1|Cell 2\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableCell | (tableBlock C e l l   1)) (tableCell | (tableBlock C e l l   2 (nl \\n))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "one row, cells on consecutive, individual lines",

                /* input */
                "|===\n" +
                "\n" +
                "|Cell 1\n" +
                "|Cell 2\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableCell | (tableBlock C e l l   1 \\n)) (tableCell | (tableBlock C e l l   2 (nl \\n))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "one row, cells on the same line and individual lines",

                /* input */
                "|===\n" +
                "\n" +
                "|Cell 1|Cell 2\n" +
                "|Cell 3\n" +
                "|Cell 4\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableCell | (tableBlock C e l l   1)) (tableCell | (tableBlock C e l l   2 \\n)) (tableCell | (tableBlock C e l l   3 \\n)) (tableCell | (tableBlock C e l l   4 (nl \\n))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "one row, cell content with varying leading and trailing spaces",

                /* input */
                "|===\n" +
                "\n" +
                "|    Cell 1 \t|\t  Cell 2  \n" +
                "|  \t  Cell 3   \t\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableCell | (tableBlock (spaces        ) C e l l   1   \\t)) (tableCell | (tableBlock (spaces \\t    ) C e l l   2     \\n)) (tableCell | (tableBlock (spaces     \\t    ) C e l l   3       \\t (nl \\n))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "two rows, one column",

                /* input */
                "|===\n" +
                "\n" +
                "|Cell 1\n" +
                "\n" +
                "|Cell 2\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableCell | (tableBlock C e l l   1 (nl \\n))) (bl \\n) (tableCell | (tableBlock C e l l   2 (nl \\n))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "two rows, two columns",

                /* input */
                "|===\n" +
                "\n" +
                "| Cell 1 | Cell 2\n" +
                "\n" +
                "| Cell 3 | Cell 4\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableCell | (tableBlock (spaces  ) C e l l   1  )) (tableCell | (tableBlock (spaces  ) C e l l   2 (nl \\n))) (bl \\n) (tableCell | (tableBlock (spaces  ) C e l l   3  )) (tableCell | (tableBlock (spaces  ) C e l l   4 (nl \\n))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cell content on two lines",

                /* input */
                "|===\n" +
                "\n" +
                "| Cell 1 | Cell 2 \n" +
                "continued\n" +
                "\n" +
                "| Cell 3 | Cell 4\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableCell | (tableBlock (spaces  ) C e l l   1  )) (tableCell | (tableBlock (spaces  ) C e l l   2   \\n c o n t i n u e d (nl \\n))) (bl \\n) (tableCell | (tableBlock (spaces  ) C e l l   3  )) (tableCell | (tableBlock (spaces  ) C e l l   4 (nl \\n))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cell with two blocks",

                /* input */
                "|===\n" +
                "\n" +
                "| Cell 1 | Cell 2\n" +
                "\n" +
                "continued\n" +
                "\n" +
                "| Cell 3 | Cell 4\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableCell | (tableBlock (spaces  ) C e l l   1  )) (tableCell | (tableBlock (spaces  ) C e l l   2 (nl \\n)) (bl \\n) (tableBlock c o n t i n u e d (nl \\n))) (bl \\n) (tableCell | (tableBlock (spaces  ) C e l l   3  )) (tableCell | (tableBlock (spaces  ) C e l l   4 (nl \\n))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            }
        });

    }

}
