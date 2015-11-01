package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AuthorLineTest extends GrammarTest {

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
                "a header with one author name",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "a header with one author name ended by EOF",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) <EOF>)))"
            },
            {
                /* message */
                "a header with one author name containing dot character",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John .Doe",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   . D o e)) <EOF>)))"
            },
            {
                /* message */
                "a header with one author name and author address",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe <jd@mail.com>\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e  ) < (authorAddress j d @ m a i l . c o m) >) \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "a header with one author name and author address ended by EOF",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe <jd@mail.com>",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e  ) < (authorAddress j d @ m a i l . c o m) >) <EOF>)))"
            },
            {
                /* message */
                "a header with two authors name",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe; Janie Roe\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) ; (author (authorName   J a n i e   R o e)) \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "a header with two authors name and address",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe <jd@mail.com>; Janie Roe <janie@mail.com>\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e  ) < (authorAddress j d @ m a i l . c o m) >) ; (author (authorName   J a n i e   R o e  ) < (authorAddress j a n i e @ m a i l . c o m) >) \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "a single comment before author line",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "// a single comment\n" +
                "John Doe",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (singleComment / /   a   s i n g l e   c o m m e n t \\n) (authors (author (authorName J o h n   D o e)) <EOF>)))"
            },
            {
                /* message */
                "two single comments before author line",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "// a single comment\n" +
                "// another comment\n" +
                "John Doe",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (singleComment / /   a   s i n g l e   c o m m e n t \\n) (singleComment / /   a n o t h e r   c o m m e n t \\n) (authors (author (authorName J o h n   D o e)) <EOF>)))"
            },
            {
                /* message */
                "a multiline comment before author line",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "////\n" +
                "a comment\n" +
                "////\n" +
                "John Doe",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (multiComment (multiCommentDelimiter / / / / \\n) a   c o m m e n t \\n (multiCommentDelimiter / / / / \\n)) (authors (author (authorName J o h n   D o e)) <EOF>)))"
            }

        });

    }

}
