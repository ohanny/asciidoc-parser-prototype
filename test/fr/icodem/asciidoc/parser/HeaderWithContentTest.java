package fr.icodem.asciidoc.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class HeaderWithContentTest extends GrammarTest {

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
                "A header with simple preamble",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\nXXX\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) (nl \\n) (preamble (block (paragraph X X X)) (nl \\n)))"
            },
            {
                /* message */
                "A header with one author name",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e) (nl \\n))))"
            },
            {
                /* message */
                "A header with one author name ended by EOF",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e) <EOF>)))"
            },
            {
                /* message */
                "A header with one author name and author address",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe <jd@mail.com>\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e  ) < (authorAddress j d @ m a i l . c o m) > (nl \\n))))"
            },
            {
                /* message */
                "A header with one author name and author address ended by EOF",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe <jd@mail.com>",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e  ) < (authorAddress j d @ m a i l . c o m) > <EOF>)))"
            },
            {
                /* message */
                "A header with two authors name",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe; Jane Dole\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e) ; (authorName   J a n e   D o l e) (nl \\n))))"
            },
            {
                /* message */
                "A header with two authors name and address",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe <jd@mail.com>; Jane Dole <janed@mail.com>\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e  ) < (authorAddress j d @ m a i l . c o m) > ; (authorName   J a n e   D o l e  ) < (authorAddress j a n e d @ m a i l . c o m) > (nl \\n))))"
            },
            {
                /* message */
                "A paragraph ended a with '== ' line that should not be a section title",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\nXXX\n" +
                "== First Section\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n)) (nl \\n) (preamble (block (paragraph X X X \\n = =   F i r s t   S e c t i o n)) (nl \\n)))"
            },
            {
                /* message */
                "No line between authors and preamble",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "A paragraph\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e) (nl \\n))) (preamble (block (paragraph A   p a r a g r a p h)) (nl \\n)))"
            }

        });

    }

}
