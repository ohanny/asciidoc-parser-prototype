package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

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
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (bl \\n) (content (preamble (block (paragraph X X X) (nl \\n)) (bl \\n)) (section = =   (sectionTitle F i r s t   S e c t i o n) \\n)) (bl <EOI>))"
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
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !)) (authors (author (authorName J o h n   D o e)))) (bl \\n) (content (section = =   (sectionTitle F i r s t   S e c t i o n) \\n)) (bl <EOI>))"
            },
            {
                /* message */
                "A paragraph directly after title in the header, containing a '== ' line that should not be a section title",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "== First Section\n" +
                "YYY",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (content (section = =   (sectionTitle F i r s t   S e c t i o n) \\n) (block (paragraph Y Y Y <EOI>))))"
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
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (bl \\n) (content (preamble (block (paragraph X X X) (nl \\n)) (bl \\n)) (section = =   (sectionTitle F i r s t   S e c t i o n) \\n) (bl \\n) (block (paragraph Y Y Y <EOI>))))"
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
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (bl \\n) (content (preamble (block (paragraph X X X) (nl \\n)) (bl \\n)) (section = =   (sectionTitle F i r s t   S e c t i o n) \\n) (bl \\n) (block (paragraph Y Y Y) (nl \\n))) (bl <EOI>))"
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
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (bl \\n) (content (preamble (block (paragraph X X X) (nl \\n)) (bl \\n) (block (paragraph Y Y Y) (nl \\n)) (bl \\n)) (section = =   (sectionTitle F i r s t   S e c t i o n) \\n) (bl \\n) (block (paragraph Z Z Z <EOI>))))"
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
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (bl \\n) (content (preamble (block (paragraph X X X) (nl \\n)) (bl \\n)) (section = =   (sectionTitle F i r s t   S e c t i o n) \\n) (bl \\n) (block (paragraph Z Z Z) (nl \\n)) (bl \\n) (block (paragraph Y Y Y <EOI>))))"
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
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (bl \\n) (content (preamble (block (paragraph X X X) (nl \\n)) (bl \\n) (block (paragraph W W W) (nl \\n)) (bl \\n)) (section = =   (sectionTitle F i r s t   S e c t i o n) \\n) (bl \\n) (block (paragraph Z Z Z) (nl \\n)) (bl \\n) (block (paragraph Y Y Y <EOI>))))"
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
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !)) (authors (author (authorName J o h n   D o e)))) (bl \\n) (content (preamble (block (paragraph W W W) (nl \\n)) (bl \\n)) (section = =   (sectionTitle F i r s t   S e c t i o n) \\n) (block (paragraph Z Z Z) (nl \\n)) (bl \\n) (block (paragraph Y Y Y <EOI>))))"
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
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !)) (authors (author (authorName J o h n   D o e)))) (bl \\n) (content (section = =   (sectionTitle F i r s t   S e c t i o n) \\n) (block (list (listItem *   (listItemValue L e m o n) \\n) (listItem *   (listItemValue C h e r r y) \\n) (listItem *   (listItemValue M a n d a r i n e) <EOI>)))))"
            }

        });

    }

}
