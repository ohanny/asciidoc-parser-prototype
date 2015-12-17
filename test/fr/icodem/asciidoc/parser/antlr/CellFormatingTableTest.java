package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CellFormatingTableTest extends GrammarTest {

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
                "cell duplicated among two columns",

                /* input */
                "|===\n" +
                "\n" +
                "|Cell 1 |Cell 2\n" +
                "\n" +
                "2*|Cell 3\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell | (tableCellContent C e l l   1 (spaces  ))) (tableCell | (tableCellContent C e l l   2 (nl \\n)))) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellSpan 2 *)) | (tableCellContent C e l l   3 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cell spanning two columns",

                /* input */
                "|===\n" +
                "\n" +
                "|Cell 1 |Cell 2\n" +
                "\n" +
                "2+|Cell 3\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell | (tableCellContent C e l l   1 (spaces  ))) (tableCell | (tableCellContent C e l l   2 (nl \\n)))) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellSpan 2 +)) | (tableCellContent C e l l   3 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cell spanning three columns and two rows",

                /* input */
                "|===\n" +
                "\n" +
                "|Cell 1|Cell 2|Cell 3|Cell 4\n" +
                "\n" +
                "3.2+|Cell 5|Cell 6\n" +
                "\n" +
                "|Cell 7\n" +
                "\n" +
                "|Cell 8|Cell 9|Cell 10|Cell 11\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell | (tableCellContent C e l l   1)) (tableCell | (tableCellContent C e l l   2)) (tableCell | (tableCellContent C e l l   3)) (tableCell | (tableCellContent C e l l   4 (nl \\n)))) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellSpan 3 . 2 +)) | (tableCellContent C e l l   5)) (tableCell | (tableCellContent C e l l   6 (nl \\n)))) (bl \\n) (tableRow (tableCell | (tableCellContent C e l l   7 (nl \\n)))) (bl \\n) (tableRow (tableCell | (tableCellContent C e l l   8)) (tableCell | (tableCellContent C e l l   9)) (tableCell | (tableCellContent C e l l   1 0)) (tableCell | (tableCellContent C e l l   1 1 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cell duplicated among three columns and two rows",

                /* input */
                "|===\n" +
                "\n" +
                "|Cell 1|Cell 2|Cell 3|Cell 4\n" +
                "\n" +
                "3.2*|Cell 5|Cell 6\n" +
                "\n" +
                "|Cell 7\n" +
                "\n" +
                "|Cell 8|Cell 9|Cell 10|Cell 11\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell | (tableCellContent C e l l   1)) (tableCell | (tableCellContent C e l l   2)) (tableCell | (tableCellContent C e l l   3)) (tableCell | (tableCellContent C e l l   4 (nl \\n)))) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellSpan 3 . 2 *)) | (tableCellContent C e l l   5)) (tableCell | (tableCellContent C e l l   6 (nl \\n)))) (bl \\n) (tableRow (tableCell | (tableCellContent C e l l   7 (nl \\n)))) (bl \\n) (tableRow (tableCell | (tableCellContent C e l l   8)) (tableCell | (tableCellContent C e l l   9)) (tableCell | (tableCellContent C e l l   1 0)) (tableCell | (tableCellContent C e l l   1 1 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cells aligned horizontally centered, left, right",

                /* input */
                "|===\n" +
                "\n" +
                "^|Cell 1 <|Cell 2 >|Cell 3\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellAlign ^)) | (tableCellContent C e l l   1 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellAlign <)) | (tableCellContent C e l l   2 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellAlign >)) | (tableCellContent C e l l   3 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cells aligned vertically centered, left, right",

                /* input */
                "|===\n" +
                "\n" +
                ".^|Cell 1 .<|Cell 2 .>|Cell 3\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellAlign . ^)) | (tableCellContent C e l l   1 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellAlign . <)) | (tableCellContent C e l l   2 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellAlign . >)) | (tableCellContent C e l l   3 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cells aligned horizontally / vertically centered, left, right",

                /* input */
                "|===\n" +
                "\n" +
                "^.^|Cell 1 <.<|Cell 2 >.>|Cell 3\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellAlign ^ . ^)) | (tableCellContent C e l l   1 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellAlign < . <)) | (tableCellContent C e l l   2 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellAlign > . >)) | (tableCellContent C e l l   3 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cells with multiple alignments",

                /* input */
                "|===\n" +
                "\n" +
                "<.^|Cell 1 ^.>|Cell 2 .>|Cell 3\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellAlign < . ^)) | (tableCellContent C e l l   1 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellAlign ^ . >)) | (tableCellContent C e l l   2 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellAlign . >)) | (tableCellContent C e l l   3 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "first cell in column italicized",

                /* input */
                "|===\n" +
                "\n" +
                "e|Cell 1 |Cell 2 |Cell 3\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellStyle e)) | (tableCellContent C e l l   1 (spaces  ))) (tableCell | (tableCellContent C e l l   2 (spaces  ))) (tableCell | (tableCellContent C e l l   3 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "middle cell in column italicized",

                /* input */
                "|===\n" +
                "\n" +
                "|Cell 1 e|Cell 2 |Cell 3\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell | (tableCellContent C e l l   1 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellStyle e)) | (tableCellContent C e l l   2 (spaces  ))) (tableCell | (tableCellContent C e l l   3 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "last cell in column italicized",

                /* input */
                "|===\n" +
                "\n" +
                "|Cell 1 |Cell 2 e|Cell 3\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell | (tableCellContent C e l l   1 (spaces  ))) (tableCell | (tableCellContent C e l l   2 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellStyle e)) | (tableCellContent C e l l   3 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cells with different style",

                /* input */
                "|===\n" +
                "\n" +
                "m|Cell 1 s|Cell 2\n" +
                "\n" +
                "|Cell 3 v|Cell 4\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellStyle m)) | (tableCellContent C e l l   1 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellStyle s)) | (tableCellContent C e l l   2 (nl \\n)))) (bl \\n) (tableRow (tableCell | (tableCellContent C e l l   3 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellStyle v)) | (tableCellContent C e l l   4 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "cells with a variety of specifiers",

                /* input */
                "|===\n" +
                "\n" +
                "2*m|Cell 1\n" +
                "\n" +
                ".3+^.>s|Cell 2 e|Cell 3\n" +
                "\n" +
                ".^l|Cell 4\n" +
                "\n" +
                "v|Cell 5\n" +
                "\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellSpan 2 *) (tableCellStyle m)) | (tableCellContent C e l l   1 (nl \\n)))) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellSpan . 3 +) (tableCellAlign ^ . >) (tableCellStyle s)) | (tableCellContent C e l l   2 (spaces  ))) (tableCell (tableCellSpecifiers (tableCellStyle e)) | (tableCellContent C e l l   3 (nl \\n)))) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellAlign . ^) (tableCellStyle l)) | (tableCellContent C e l l   4 (nl \\n)))) (bl \\n) (tableRow (tableCell (tableCellSpecifiers (tableCellStyle v)) | (tableCellContent C e l l   5 (nl \\n)))) (bl \\n) (tableDelimiter | = = = <EOF>)))))"
            },
            {
                /* message */
                "one row, cells on consecutive, individual lines",

                /* input */
                "|===\n" +
                "|Cell 1\n" +
                "a|Cell 2\n" +
                "|===",

                /* expected */
                "(document (content (block (table (tableDelimiter | = = = \\n) (tableRow (tableCell | (tableCellContent C e l l   1 (nl \\n))) (tableCell (tableCellSpecifiers (tableCellStyle a)) | (tableCellContent C e l l   2 (nl \\n)))) (tableDelimiter | = = = <EOF>)))))"
            }
        });

    }

}
