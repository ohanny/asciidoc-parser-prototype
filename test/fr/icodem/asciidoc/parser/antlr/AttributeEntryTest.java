package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AttributeEntryTest extends GrammarTest {

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
                "an attribute entry",

                /* input */
                ":fruit: kiwi",

                /* expected */
                "(document (body (attributeEntry : (attributeName f r u i t) :   (attributeValue (attributeValuePart k i w i)) <EOF>)))"
            },
            {
                /* message */
                "an attribute entry ended by new line",

                /* input */
                ":fruit: kiwi\n",

                /* expected */
                "(document (body (attributeEntry : (attributeName f r u i t) :   (attributeValue (attributeValuePart k i w i)) \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "two attribute entries",

                /* input */
                ":fruit: kiwi\n" +
                ":vegetable: cabbage",

                /* expected */
                "(document (body (attributeEntry : (attributeName f r u i t) :   (attributeValue (attributeValuePart k i w i)) \\n) (attributeEntry : (attributeName v e g e t a b l e) :   (attributeValue (attributeValuePart c a b b a g e)) <EOF>)))"
            },
            {
                /* message */
                "attribute entry with no value",

                /* input */
                ":fruit:",

                /* expected */
                "(document (body (attributeEntry : (attributeName f r u i t) : <EOF>)))"
            },
            {
                /* message */
                "attribute value continued on next line",

                /* input */
                ":fruit: kiwi+\n" +
                "fruit",

                /* expected */
                "(document (body (attributeEntry : (attributeName f r u i t) :   (attributeValue (attributeValuePart k i w i) + \\n (attributeValuePart f r u i t)) <EOF>)))"
            },
            {
                /* message */
                "attribute value continued on next line with leading spaces",

                /* input */
                ":fruit: kiwi+\n" +
                "   fruit",

                /* expected */
                "(document (body (attributeEntry : (attributeName f r u i t) :   (attributeValue (attributeValuePart k i w i) + \\n       (attributeValuePart f r u i t)) <EOF>)))"
            },
            {
                /* message */
                "unset attribute with leading bang",

                /* input */
                ":!fruit:",

                /* expected */
                "(document (body (attributeEntry : ! (attributeName f r u i t) : <EOF>)))"
            },
            {
                /* message */
                "unset attribute with trailing bang",

                /* input */
                ":fruit!:",

                /* expected */
                "(document (body (attributeEntry : (attributeName f r u i t) ! : <EOF>)))"
            }
        });

    }

}
