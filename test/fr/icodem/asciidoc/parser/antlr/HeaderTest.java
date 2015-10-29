package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HeaderTest extends GrammarTest {

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
                "header with author and attribute entry ended by EOF",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                ":fruit: kiwi",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e) \\n) (attributeEntry : (attributeName f r u i t) :   (attributeValue (attributeValuePart k i w i)) <EOF>)))"
            },
            {
                /* message */
                "header with author and attribute entry ended by new line",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "John Doe\n" +
                ":fruit: kiwi\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (authors (authorName J o h n   D o e) \\n) (attributeEntry : (attributeName f r u i t) :   (attributeValue (attributeValuePart k i w i)) \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "header with attribute entry ended by EOF",

                /* input */
                "= Hello, AsciiDoc!\n" +
                ":fruit: kiwi",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (attributeEntry : (attributeName f r u i t) :   (attributeValue (attributeValuePart k i w i)) <EOF>)))"
            },
            {
                /* message */
                "header with attribute entry ended by new line",

                /* input */
                "= Hello, AsciiDoc!\n" +
                ":fruit: kiwi\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) (attributeEntry : (attributeName f r u i t) :   (attributeValue (attributeValuePart k i w i)) \\n)) (bl <EOF>))"
            }
        });

    }

}
