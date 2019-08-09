package fr.icodem.asciidoc.parser.antlr;

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParagraphTest extends GrammarTest {

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
                "a simple paragraph",

                /* input */
                "This is some content",

                /* expected */
                "(document (content (preamble (block (paragraph T h i s   i s   s o m e   c o n t e n t <EOI>)))))"
            },
            {
                /* message */
                "a paragraph ending with new line",

                /* input */
                "This is some content \n",

                /* expected */
                "(document (content (preamble (block (paragraph T h i s   i s   s o m e   c o n t e n t  ) (nl \\n)))) (bl <EOI>))"
            },
            {
                /* message */
                "two lines paragraph ended by EOF",

                /* input */
                "This is some content \n" +
                "on two lines",

                /* expected */
                "(document (content (preamble (block (paragraph T h i s   i s   s o m e   c o n t e n t   \\n o n   t w o   l i n e s <EOI>)))))"
            },
            {
                /* message */
                "two lines paragraph ended by new line",

                /* input */
                "This is some content \n" +
                "on two lines\n",

                /* expected */
                "(document (content (preamble (block (paragraph T h i s   i s   s o m e   c o n t e n t   \\n o n   t w o   l i n e s) (nl \\n)))) (bl <EOI>))"
            },
            {
                /* message */
                "a paragraph containing '/' and '//'",

                /* input */
                "This is some content with / and // is it ok ?",

                /* expected */
                "(document (content (preamble (block (paragraph T h i s   i s   s o m e   c o n t e n t   w i t h   /   a n d   / /   i s   i t   o k   ? <EOI>)))))"
            },
            {
                /* message */
                "a paragraph containing '////'",

                /* input */
                "This is some content with //// is it ok ?",

                /* expected */
                "(document (content (preamble (block (paragraph T h i s   i s   s o m e   c o n t e n t   w i t h   / / / /   i s   i t   o k   ? <EOI>)))))"
            },
            {
                /* message */
                "a paragraph containing '[' and '[['",

                /* input */
                "This is some content with [ and [[ is it ok ?",

                /* expected */
                "(document (content (preamble (block (paragraph T h i s   i s   s o m e   c o n t e n t   w i t h   [   a n d   [ [   i s   i t   o k   ? <EOI>)))))"
            },
            {
                /* message */
                "a paragraph containing ']' and ']]'",

                /* input */
                "This is some content with ] and ]] is it ok ?",

                /* expected */
                "(document (content (preamble (block (paragraph T h i s   i s   s o m e   c o n t e n t   w i t h   ]   a n d   ] ]   i s   i t   o k   ? <EOI>)))))"
            },
            {
                /* message */
                "two paragraphs separated by a single line comment",

                /* input */
                "This is a first paragraph\n" +
                "// comment\n" +
                "This is a second paragraph",

                /* expected */
                "(document (content (preamble (block (paragraph T h i s   i s   a   f i r s t   p a r a g r a p h \\n)) (block (singleComment / /   c o m m e n t \\n)) (block (paragraph T h i s   i s   a   s e c o n d   p a r a g r a p h <EOI>)))))"
            },
            {
                /* message */
                "two paragraphs separated by a multi line comment",

                /* input */
                "This is a first paragraph\n" +
                "////\n" +
                "comment\n" +
                "////\n" +
                "This is a second paragraph",

                /* expected */
                "(document (content (preamble (block (paragraph T h i s   i s   a   f i r s t   p a r a g r a p h \\n)) (block (multiComment (multiCommentDelimiter / / / / \\n) c o m m e n t \\n (multiCommentDelimiter / / / / \\n))) (block (paragraph T h i s   i s   a   s e c o n d   p a r a g r a p h <EOI>)))))"
            },
            {
                /* message */
                "should not be a first level title",

                /* input */
                "=Banana\n",

                /* expected */
                "(document (content (preamble (block (paragraph = B a n a n a) (nl \\n)))) (bl <EOI>))"
            },
            {
                /* message */
                "should not be a second level title",

                /* input */
                "==Banana\n",

                /* expected */
                "(document (content (preamble (block (paragraph = = B a n a n a) (nl \\n)))) (bl <EOI>))"
            },
            {
                /* message */
                "paragraph ended with \\r\\n",

                /* input */
                "= Fruits\r\n" +
                "\r\n" +
                "About fruits\r\n" +
                "\r\n" +
                "== Banana\r\n",

                /* expected */
                "(document (header (documentSection (documentTitle F r u i t s \\r))) (bl \\r \\n) (content (preamble (block (paragraph A b o u t   f r u i t s) (nl \\r \\n)) (bl \\r \\n)) (section = =   (sectionTitle B a n a n a \\r) \\n)) (bl <EOI>))"
            },
            {
                /* message */
                "paragraph with '+' character inside",

                /* input */
                "the + sign",

                /* expected */
                "(document (content (preamble (block (paragraph t h e   +   s i g n <EOI>)))))"
            }

        });

    }

}
