package fr.icodem.asciidoc.parser;

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
                "An attribute entry",

                /* input */
                ":fruit: kiwi",

                /* expected */
                "(document (attributeEntry : (attributeName f r u i t) :   (attributeValue k i w i) <EOF>))"
            },
            {
                /* message */
                "An attribute entry ended by new line",

                /* input */
                ":fruit: kiwi\n",

                /* expected */
                "(document (attributeEntry : (attributeName f r u i t) :   (attributeValue k i w i) \\n))"
            },
            {
                /* message */
                "Two attribute entries",

                /* input */
                ":fruit: kiwi\n" +
                ":vegetable: cabbage",

                /* expected */
                "(document (attributeEntry : (attributeName f r u i t) :   (attributeValue k i w i) \\n) (attributeEntry : (attributeName v e g e t a b l e) :   (attributeValue c a b b a g e) <EOF>))"
            }
        });

    }

}
