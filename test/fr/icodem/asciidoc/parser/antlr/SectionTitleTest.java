package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SectionTitleTest extends GrammarTest {

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
                "a document title ended with end of file",

                /* input */
                "= Hello, AsciiDoc!",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))))"
            },
            {
                /* message */
                "a document title ended with new line",

                /* input */
                "= Hello, AsciiDoc!\n",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (bl <EOI>))"
            },
            {
                /* message */
                "a document title followed by a level 1 section title",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\n" +
                "= First Section",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (bl \\n) (content (section =   (sectionTitle F i r s t   S e c t i o n) <EOI>)))"
            },
            {
                /* message */
                "a document title followed by a level 1 section title - no blank line between titles",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "== First Section",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (content (section = =   (sectionTitle F i r s t   S e c t i o n) <EOI>)))"
            },
            {
                /* message */
                "a document title followed by a level 1 section title - new line at the end",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "== First Section\n",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (content (section = =   (sectionTitle F i r s t   S e c t i o n) \\n)) (bl <EOI>))"
            },
            {
                /* message */
                "a document title followed by a a level 0 section title",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "= First Section\n",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (content (section =   (sectionTitle F i r s t   S e c t i o n) \\n)) (bl <EOI>))"
            },
            {
                /* message */
                "a document title and a few section titles - no blank line between titles",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "== First Section\n" +
                "=== Section 1.1\n" +
                "=== Section 1.2\n" +
                "== Second Section\n" +
                "=== Section 2.1\n" +
                "=== Section 2.2\n",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (content (section = =   (sectionTitle F i r s t   S e c t i o n) \\n) (section = = =   (sectionTitle S e c t i o n   1 . 1) \\n) (section = = =   (sectionTitle S e c t i o n   1 . 2) \\n) (section = =   (sectionTitle S e c o n d   S e c t i o n) \\n) (section = = =   (sectionTitle S e c t i o n   2 . 1) \\n) (section = = =   (sectionTitle S e c t i o n   2 . 2) \\n)) (bl <EOI>))"
            },
            {
                /* message */
                "a document title and a few section titles - blank lines between titles",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\n" +
                "== First Section\n" +
                "  \n" +
                "=== Section 1.1\n" +
                "\t\n" +
                "=== Section 1.2\n" +
                "\n" +
                "== Second Section\n" +
                "\n" +
                "=== Section 2.1\n" +
                "\n" +
                "=== Section 2.2",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (bl \\n) (content (section = =   (sectionTitle F i r s t   S e c t i o n) \\n) (bl     \\n) (section = = =   (sectionTitle S e c t i o n   1 . 1) \\n) (bl \\t \\n) (section = = =   (sectionTitle S e c t i o n   1 . 2) \\n) (bl \\n) (section = =   (sectionTitle S e c o n d   S e c t i o n) \\n) (bl \\n) (section = = =   (sectionTitle S e c t i o n   2 . 1) \\n) (bl \\n) (section = = =   (sectionTitle S e c t i o n   2 . 2) <EOI>)))"
            },
            {
                /* message */
                "a title containing '='",

                /* input */
                "= Title with = is it ok ?\n",

                /* expected */
                "(document (header (documentSection (documentTitle T i t l e   w i t h   =   i s   i t   o k   ?))) (bl <EOI>))"
            },
            {
                /* message */
                "a title containing '===='",

                /* input */
                "= Title with ==== is it ok ?\n",

                /* expected */
                "(document (header (documentSection (documentTitle T i t l e   w i t h   = = = =   i s   i t   o k   ?))) (bl <EOI>))"
            },
            {
                /* message */
                "a title containing '/'",

                /* input */
                "= Title with / is it ok ?\n",

                /* expected */
                "(document (header (documentSection (documentTitle T i t l e   w i t h   /   i s   i t   o k   ?))) (bl <EOI>))"
            },
            {
                /* message */
                "a title containing '//'",

                /* input */
                "= Title with // is it ok ?\n",

                /* expected */
                "(document (header (documentSection (documentTitle T i t l e   w i t h   / /   i s   i t   o k   ?))) (bl <EOI>))"
            },
            {
                /* message */
                "a title containing '[' and '[['",

                /* input */
                "= Title with [ and [[ is it ok ?\n",

                /* expected */
                "(document (header (documentSection (documentTitle T i t l e   w i t h   [   a n d   [ [   i s   i t   o k   ?))) (bl <EOI>))"
            },
            {
                /* message */
                "a title containing ']' and ']]'",

                /* input */
                "= Title with ] and ]] is it ok ?\n",

                /* expected */
                "(document (header (documentSection (documentTitle T i t l e   w i t h   ]   a n d   ] ]   i s   i t   o k   ?))) (bl <EOI>))"
            },
            {
                /* message */
                "a title with '[hello]'",

                /* input */
                "= Title with [hello] is it ok ?\n",

                /* expected */
                "(document (header (documentSection (documentTitle T i t l e   w i t h   [ h e l l o ]   i s   i t   o k   ?))) (bl <EOI>))"
            },
            {
                /* message */
                "a title with '[[hello]]'",

                /* input */
                "= Title with [[hello]] is it ok ?\n",

                /* expected */
                "(document (header (documentSection (documentTitle T i t l e   w i t h   [ [ h e l l o ] ]   i s   i t   o k   ?))) (bl <EOI>))"
            },
            {
                /* message */
                "a title with attribute list",

                /* input */
                "= Title 1\n" +
                "\n" +
                "[.att1]\n" +
                "== Title 2\n",

                /* expected */
                "(document (header (documentSection (documentTitle T i t l e   1))) (bl \\n) (content (preamble (attributeList [ (roleAttribute . (attributeName a t t 1)) ] \\n)) (section = =   (sectionTitle T i t l e   2) \\n)) (bl <EOI>))"
            }

        });

    }

}
