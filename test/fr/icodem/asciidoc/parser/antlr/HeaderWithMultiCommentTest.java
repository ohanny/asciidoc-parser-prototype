package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HeaderWithMultiCommentTest extends GrammarTest {

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
                "Header with one comment block that ends with new line",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "////\n" +
                "This is a comment...\n" +
                "...on two lines\n" +
                "////\n",

                /* expected */
                "(document (header (documentTitle (documentTitleValue H e l l o ,   A s c i i D o c !)) (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t . . . \\n . . . o n   t w o   l i n e s \\n (multiCommentDelimiter / / / / \\n))) (bl <EOI>))"
            },
            {
                /* message */
                "Header with one comment block that ends with EOF",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "////\n" +
                "This is a comment...\n" +
                "...on two lines\n" +
                "////",

                /* expected */
                "(document (header (documentTitle (documentTitleValue H e l l o ,   A s c i i D o c !)) (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t . . . \\n . . . o n   t w o   l i n e s \\n (multiCommentDelimiter / / / / <EOI>))))"
            },
            {
                /* message */
                "Header with one comment block containing multiple '/'",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "////\n" +
                "This is a comment with // and /// ...\n" +
                "and /// and //// and that's all\n" +
                "////\n",

                /* expected */
                "(document (header (documentTitle (documentTitleValue H e l l o ,   A s c i i D o c !)) (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t   w i t h   / /   a n d   / / /   . . . \\n a n d   / / /   a n d   / / / /   a n d   t h a t ' s   a l l \\n (multiCommentDelimiter / / / / \\n))) (bl <EOI>))"
            },
            {
                /* message */
                "Header with one comment block containing multiple '/' at the beginning of lines",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "////\n" +
                "/ one leading slash\n" +
                "// two leading slashes\n" +
                "/// three leading slashes\n" +
                "//// four leading slashes\n" +
                "////\n",

                /* expected */
                "(document (header (documentTitle (documentTitleValue H e l l o ,   A s c i i D o c !)) (multiComment (multiCommentDelimiter / / / / \\n) /   o n e   l e a d i n g   s l a s h \\n / /   t w o   l e a d i n g   s l a s h e s \\n / / /   t h r e e   l e a d i n g   s l a s h e s \\n / / / /   f o u r   l e a d i n g   s l a s h e s \\n (multiCommentDelimiter / / / / \\n))) (bl <EOI>))"
            },
            {
                /* message */
                "Header with two contiguous comment blocks",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "////\n" +
                "comment 1\n" +
                "////\n" +
                "////\n" +
                "comment 2\n" +
                "////",

                /* expected */
                "(document (header (documentTitle (documentTitleValue H e l l o ,   A s c i i D o c !)) (multiComment (multiCommentDelimiter / / / / \\n) c o m m e n t   1 \\n (multiCommentDelimiter / / / / \\n)) (multiComment (multiCommentDelimiter / / / / \\n) c o m m e n t   2 \\n (multiCommentDelimiter / / / / <EOI>))))"
            },
            {
                /* message */
                "Header with author name between two multi line comment",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "////\n" +
                "comment 1\n" +
                "////\n" +
                "John Doe\n" +
                "////\n" +
                "comment 2\n" +
                "////",

                /* expected */
                "(document (header (documentTitle (documentTitleValue H e l l o ,   A s c i i D o c !)) (multiComment (multiCommentDelimiter / / / / \\n) c o m m e n t   1 \\n (multiCommentDelimiter / / / / \\n)) (authors (author (authorName J o h n   D o e))) (multiComment (multiCommentDelimiter / / / / \\n) c o m m e n t   2 \\n (multiCommentDelimiter / / / / <EOI>))))"
            },
            {
                /* message */
                "A comment block before header title",

                /* input */
                "////\n" +
                "This is a comment \n" +
                "////\n" +
                "= Hello, AsciiDoc!\n",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t   \\n (multiCommentDelimiter / / / / \\n)) (header (documentTitle (documentTitleValue H e l l o ,   A s c i i D o c !))) (bl <EOI>))"
            },
            {
                /* message */
                "A comment block and a few new lines before header title",

                /* input */
                "\n" +
                "\n" +
                "////\n" +
                "This is a comment \n" +
                "////\n" +
                "\n" +
                "= Hello, AsciiDoc!\n",

                /* expected */
                "(document (bl \\n) (bl \\n) (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t   \\n (multiCommentDelimiter / / / / \\n)) (bl \\n) (header (documentTitle (documentTitleValue H e l l o ,   A s c i i D o c !))) (bl <EOI>))"
            }
        });

    }

}
