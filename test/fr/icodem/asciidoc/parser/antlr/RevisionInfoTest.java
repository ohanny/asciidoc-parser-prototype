package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RevisionInfoTest extends GrammarTest {

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
                "a header with revision info ended by EOF",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "v1.0",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) \\n) (revisionInfo v 1 . 0 <EOF>)))"
            },
            {
                /* message */
                "a header with revision info ended by one new line",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "v1.0\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) \\n) (revisionInfo v 1 . 0 \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "a header with revision info ended by two new lines",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "v1.0\n\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) \\n) (revisionInfo v 1 . 0 \\n)) (bl \\n) (bl <EOF>))"
            },
            {
                /* message */
                "a header with revision info on two lines",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "v1.0, October 2, 2013\n" +
                ": First incarnation",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) \\n) (revisionInfo v 1 . 0 ,   O c t o b e r   2 ,   2 0 1 3 \\n :   F i r s t   i n c a r n a t i o n <EOF>)))"
            },
            {
                /* message */
                "a single comment before revision info",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "// a comment\n" +
                "v1.0, October 2, 2013\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) \\n) (singleComment / /   a   c o m m e n t \\n) (revisionInfo v 1 . 0 ,   O c t o b e r   2 ,   2 0 1 3 \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "two single comments before revision info",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "// a comment\n" +
                "// another comment\n" +
                "v1.0, October 2, 2013\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) \\n) (singleComment / /   a   c o m m e n t \\n) (singleComment / /   a n o t h e r   c o m m e n t \\n) (revisionInfo v 1 . 0 ,   O c t o b e r   2 ,   2 0 1 3 \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "a multi-line comment before revision info",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "////\n" +
                "a comment\n" +
                "////\n" +
                "v1.0, October 2, 2013\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) \\n) (multiComment (multiCommentDelimiter / / / / \\n) a   c o m m e n t \\n (multiCommentDelimiter / / / / \\n)) (revisionInfo v 1 . 0 ,   O c t o b e r   2 ,   2 0 1 3 \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "a single comment after revision info",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "v1.0\n" +
                "// a comment",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) \\n) (revisionInfo v 1 . 0 \\n)) (singleComment / /   a   c o m m e n t <EOF>))"
            },
            {
                /* message */
                "an attribute entry after revision info",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                "v1.0\n" +
                "v1.0, October 2, 2013\n" +
                ":fruit: kiwi",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (author (authorName J o h n   D o e)) \\n) (revisionInfo v 1 . 0 \\n v 1 . 0 ,   O c t o b e r   2 ,   2 0 1 3 \\n) (attributeEntry : (attributeName f r u i t) :   (attributeValueParts (attributeValuePart k i w i)) <EOF>)))"
            }
        });

    }

}
