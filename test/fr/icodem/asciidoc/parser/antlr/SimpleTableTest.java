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
                "cells on the same line",

                /* input */
                "|===\n" +
                "|Cell 1|Cell 2\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (tableCell | (tableCellContent C e l l   1)) (tableCell | (tableCellContent C e l l   2 (nl \\n))) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cells on consecutive, individual lines",

                /* input */
                "|===\n" +
                "|Cell 1\n" +
                "|Cell 2\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (tableCell | (tableCellContent C e l l   1 (nl \\n))) (tableCell | (tableCellContent C e l l   2 (nl \\n))) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cells on the same line and individual lines",

                /* input */
                "|===\n" +
                "|Cell 1|Cell 2\n" +
                "|Cell 3\n" +
                "|Cell 4\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (tableCell | (tableCellContent C e l l   1)) (tableCell | (tableCellContent C e l l   2 (nl \\n))) (tableCell | (tableCellContent C e l l   3 (nl \\n))) (tableCell | (tableCellContent C e l l   4 (nl \\n))) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cell content with varying leading and trailing spaces",

                /* input */
                "|===\n" +
                "|    Cell 1 \t|\t  Cell 2  \n" +
                "|  \t  Cell 3   \t\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (tableCell | (tableCellContent (spaces        ) C e l l   1 (spaces   \\t))) (tableCell | (tableCellContent (spaces \\t    ) C e l l   2 (spaces    ) (nl \\n))) (tableCell | (tableCellContent (spaces     \\t    ) C e l l   3 (spaces       \\t) (nl \\n))) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cells on individual lines, separated by a blank line",

                /* input */
                "|===\n" +
                "|Cell 1\n" +
                "\n" +
                "|Cell 2\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (tableCell | (tableCellContent C e l l   1 (nl \\n))) (bl \\n) (tableCell | (tableCellContent C e l l   2 (nl \\n))) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cell lines separated by blank lines",

                /* input */
                "|===\n" +
                "\n" +
                "| Cell 1 | Cell 2\n" +
                "\n" +
                "| Cell 3 | Cell 4\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableCell | (tableCellContent (spaces  ) C e l l   1 (spaces  ))) (tableCell | (tableCellContent (spaces  ) C e l l   2 (nl \\n))) (bl \\n) (tableCell | (tableCellContent (spaces  ) C e l l   3 (spaces  ))) (tableCell | (tableCellContent (spaces  ) C e l l   4 (nl \\n))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            }
        });

    }

}
