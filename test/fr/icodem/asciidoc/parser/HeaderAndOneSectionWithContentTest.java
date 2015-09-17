package fr.icodem.asciidoc.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HeaderAndOneSectionWithContentTest extends GrammarTest {

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
                "A header with preamble followed by a level 1 section with no content",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\nXXX\n" +
                "\n" +
                "== First Section\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) (nl \\n) (preamble (block (paragraph X X X)) (nl \\n) (nl \\n)) (section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n)))"
            },
            {
                /* message */
                "A header with author name, followed by a level 1 section with no content",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "\n" +
                "== First Section\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e) (nl \\n))) (nl \\n) (section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n)))"
            },
            {
                /* message */
                "A paragraph directly after title in the header, containing a '== ' line that should not be a section title",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "== First Section\n" +
                "YYY",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) (section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n) (block (paragraph Y Y Y))))"
            },
            {
                /* message */
                "A header with one paragraph and a section with one paragraph ended by EOF",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\nXXX\n" +
                "\n" +
                "== First Section\n" +
                "\nYYY",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) (nl \\n) (preamble (block (paragraph X X X)) (nl \\n) (nl \\n)) (section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n) (nl \\n) (block (paragraph Y Y Y))))"
            },
            {
                /* message */
                "A header with one paragraph and a section with one paragraph ended by new line",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\nXXX\n" +
                "\n" +
                "== First Section\n" +
                "\nYYY\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) (nl \\n) (preamble (block (paragraph X X X)) (nl \\n) (nl \\n)) (section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n) (nl \\n) (block (paragraph Y Y Y)) (nl \\n)))"
            },
            {
                /* message */
                "A header with two paragraphs and a section with one paragraph",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\nXXX\n" +
                "\nYYY\n" +
                "\n" +
                "== First Section\n" +
                "\nZZZ",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) (nl \\n) (preamble (block (paragraph X X X)) (nl \\n) (nl \\n) (block (paragraph Y Y Y)) (nl \\n) (nl \\n)) (section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n) (nl \\n) (block (paragraph Z Z Z))))"
            },
            {
                /* message */
                "A header with one paragraph and a section with two paragraphs",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\nXXX\n" +
                "\n" +
                "== First Section\n" +
                "\nZZZ\n" +
                "\nYYY",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) (nl \\n) (preamble (block (paragraph X X X)) (nl \\n) (nl \\n)) (section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n) (nl \\n) (block (paragraph Z Z Z)) (nl \\n) (nl \\n) (block (paragraph Y Y Y))))"
            },
            {
                /* message */
                "A header with two paragraphs and a section with two paragraphs",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\nXXX\n" +
                "\nWWW\n" +
                "\n" +
                "== First Section\n" +
                "\nZZZ\n" +
                "\nYYY",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) (nl \\n) (preamble (block (paragraph X X X)) (nl \\n) (nl \\n) (block (paragraph W W W)) (nl \\n) (nl \\n)) (section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n) (nl \\n) (block (paragraph Z Z Z)) (nl \\n) (nl \\n) (block (paragraph Y Y Y))))"
            },
            {
                /* message */
                "A header with author name + preamble and a section with two paragraphs where paragraphs begin directly after titles (no new line before paragraphs)",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "\nWWW\n" +
                "\n" +
                "== First Section\n" +
                "ZZZ\n" +
                "\nYYY",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e) (nl \\n))) (nl \\n) (preamble (block (paragraph W W W)) (nl \\n) (nl \\n)) (section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n) (block (paragraph Z Z Z)) (nl \\n) (nl \\n) (block (paragraph Y Y Y))))"
            },
            {
                /* message */
                "Header, one section and unordered list",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "\n" +
                "== First Section\n" +
                "* Lemon\n" +
                "* Cherry\n" +
                "* Mandarine",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e) (nl \\n))) (nl \\n) (section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n) (block (unorderedList (listItem *   (listItemValue L e m o n) \\n) (listItem *   (listItemValue C h e r r y) \\n) (listItem *   (listItemValue M a n d a r i n e) <EOF>)))))"
            }

        });

    }

}
