package fr.icodem.asciidoc.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MultiCommentTest extends GrammarTest {

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
                "One comment block with one line. The block ends with new line.",

                /* input */
                "////\n" +
                "This is a comment\n" +
                "////\n",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t \\n (multiCommentDelimiter / / / / \\n)))"
            },
            {
                /* message */
                "One comment block with one line. The block ends with EOF.",

                /* input */
                "////\n" +
                "This is a comment\n" +
                "////",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t \\n (multiCommentDelimiter / / / / <EOF>)))"
            },
            {
                /* message */
                "One comment block with a few lines. The block ends with new line.",

                /* input */
                "////\n" +
                "First line\n" +
                "Second line\n" +
                "Third line\n" +
                "////\n",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) F i r s t   l i n e \\n S e c o n d   l i n e \\n T h i r d   l i n e \\n (multiCommentDelimiter / / / / \\n)))"
            },
            {
                /* message */
                "One comment block with two lines that ends with EOF",

                /* input */
                "////\n" +
                "This is a comment...\n" +
                "...on two lines\n" +
                "////",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t . . . \\n . . . o n   t w o   l i n e s \\n (multiCommentDelimiter / / / / <EOF>)))"
            },
            {
                /* message */
                "Two contiguous comment blocks",

                /* input */
                "////\n" +
                "comment 1\n" +
                "////\n" +
                "////\n" +
                "comment 2\n" +
                "////",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) c o m m e n t   1 \\n (multiCommentDelimiter / / / / \\n)) (multiComment (multiCommentDelimiter / / / / \\n) c o m m e n t   2 \\n (multiCommentDelimiter / / / / <EOF>)))"
            },
            {
                /* message */
                "One comment block containing multiple '/'",

                /* input */
                "////\n" +
                "This is a comment with // and /// ...\n" +
                "and /// and //// and that's all\n" +
                "////\n",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t   w i t h   / /   a n d   / / /   . . . \\n a n d   / / /   a n d   / / / /   a n d   t h a t ' s   a l l \\n (multiCommentDelimiter / / / / \\n)))"
            },
            {
                /* message */
                "One comment block containing multiple '/' at the beginning of lines",

                /* input */
                "////\n" +
                "/ one leading slash\n" +
                "// two leading slashes\n" +
                "/// three leading slashes\n" +
                "//// four leading slashes\n" +
                "////\n",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) /   o n e   l e a d i n g   s l a s h \\n / /   t w o   l e a d i n g   s l a s h e s \\n / / /   t h r e e   l e a d i n g   s l a s h e s \\n / / / /   f o u r   l e a d i n g   s l a s h e s \\n (multiCommentDelimiter / / / / \\n)))"
            },
            {
                /* message */
                "One comment block containing '[' and '[['",

                /* input */
                "////\n" +
                "This is a comment with [ and [[ ...\n" +
                "////\n",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t   w i t h   [   a n d   [ [   . . . \\n (multiCommentDelimiter / / / / \\n)))"
            },
            {
                /* message */
                "One comment block containing ']' and ']]'",

                /* input */
                "////\n" +
                "This is a comment with ] and ]] ...\n" +
                "////\n",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t   w i t h   ]   a n d   ] ]   . . . \\n (multiCommentDelimiter / / / / \\n)))"
            },
            {
                /* message */
                "One comment block containing '[hello]'",

                /* input */
                "////\n" +
                "This is a comment with [hello] ...\n" +
                "////\n",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t   w i t h   [ h e l l o ]   . . . \\n (multiCommentDelimiter / / / / \\n)))"
            },
            {
                /* message */
                "One comment block containing '[[hello]]'",

                /* input */
                "////\n" +
                "This is a comment with [[hello]] ...\n" +
                "////\n",

                /* expected */
                "(document (multiComment (multiCommentDelimiter / / / / \\n) T h i s   i s   a   c o m m e n t   w i t h   [ [ h e l l o ] ]   . . . \\n (multiCommentDelimiter / / / / \\n)))"
            }
        });

    }

}
