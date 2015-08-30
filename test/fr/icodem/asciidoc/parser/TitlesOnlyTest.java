package fr.icodem.asciidoc.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class TitlesOnlyTest extends GrammarTest {

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
                "A document title ended with end of file",

                /* input */
                "= Hello, AsciiDoc!",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) <EOF>)))"
            },
            {
                /* message */
                "A document title ended with new line",

                /* input */
                "= Hello, AsciiDoc!\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)))"
            },
            {
                /* message */
                "A section title ended without new line",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "== First Section",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) " +
                "(section (sectionTitle = =   (title F i r s t   S e c t i o n) <EOF>))" +
                ")"
            },
            {
                /* message */
                "A section title ended with new line",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "== First Section\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) " +
                "(section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n))" +
                ")"
            },
            {
                /* message */
                "A header title and a level 0 section title",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "= First Section\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) (section (sectionTitle =   (title F i r s t   S e c t i o n) \\n)))"
            },
            {
                /* message */
                "A header title and a few section titles",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "== First Section\n" +
                "=== Section 1.1\n" +
                "=== Section 1.2\n" +
                "== Second Section\n" +
                "=== Section 2.1\n" +
                "=== Section 2.2\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) (section (sectionTitle = =   (title F i r s t   S e c t i o n) \\n)) (section (sectionTitle = = =   (title S e c t i o n   1 . 1) \\n)) (section (sectionTitle = = =   (title S e c t i o n   1 . 2) \\n)) (section (sectionTitle = =   (title S e c o n d   S e c t i o n) \\n)) (section (sectionTitle = = =   (title S e c t i o n   2 . 1) \\n)) (section (sectionTitle = = =   (title S e c t i o n   2 . 2) \\n)))"
            },
            {
                /* message */
                "A header title with '='",

                /* input */
                "= Title with = is it ok ?\n",

                /* expected */
                "(document (header (documentTitle =   (title T i t l e   w i t h   =   i s   i t   o k   ?) \\n)))"
            },
            {
                /* message */
                "A header title with '===='",

                /* input */
                "= Title with ==== is it ok ?\n",

                /* expected */
                "(document (header (documentTitle =   (title T i t l e   w i t h   = = = =   i s   i t   o k   ?) \\n)))"
            },
            {
                /* message */
                "A header title with '/'",

                /* input */
                "= Title with / is it ok ?\n",

                /* expected */
                "(document (header (documentTitle =   (title T i t l e   w i t h   /   i s   i t   o k   ?) \\n)))"
            },
            {
                /* message */
                "A header title with '//'",

                /* input */
                "= Title with // is it ok ?\n",

                /* expected */
                "(document (header (documentTitle =   (title T i t l e   w i t h   / /   i s   i t   o k   ?) \\n)))"
            },
            {
                /* message */
                "A header title with '[' and '[['",

                /* input */
                "= Title with [ and [[ is it ok ?\n",

                /* expected */
                "(document (header (documentTitle =   (title T i t l e   w i t h   [   a n d   [ [   i s   i t   o k   ?) \\n)))"
            },
            {
                /* message */
                "A header title with ']' and ']]'",

                /* input */
                "= Title with ] and ]] is it ok ?\n",

                /* expected */
                "(document (header (documentTitle =   (title T i t l e   w i t h   ]   a n d   ] ]   i s   i t   o k   ?) \\n)))"
            },
            {
                /* message */
                "A header title with '[hello]'",

                /* input */
                "= Title with [hello] is it ok ?\n",

                /* expected */
                "(document (header (documentTitle =   (title T i t l e   w i t h   [ h e l l o ]   i s   i t   o k   ?) \\n)))"
            },
            {
                /* message */
                "A header title with '[[hello]]'",

                /* input */
                "= Title with [[hello]] is it ok ?\n",

                /* expected */
                "(document (header (documentTitle =   (title T i t l e   w i t h   [ [ h e l l o ] ]   i s   i t   o k   ?) \\n)))"
            }

        });

    }

}
