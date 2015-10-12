package fr.icodem.asciidoc.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AttributeListTest extends GrammarTest {

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
                "a single positional attribute",

                /* input */
                "[att1]",

                /* expected */
                "(document (attributeList [ (positionalAttribute (attributeName a t t 1)) ] <EOF>))"
            },
            {
                /* message */
                "two positional attributes",

                /* input */
                "[att1,att2]",

                /* expected */
                "(document (attributeList [ (positionalAttribute (attributeName a t t 1)) , (positionalAttribute (attributeName a t t 2)) ] <EOF>))"
            },
            {
                /* message */
                "two positional attributes, space after comma",

                /* input */
                "[att1, att2]",

                /* expected */
                "(document (attributeList [ (positionalAttribute (attributeName a t t 1)) ,   (positionalAttribute (attributeName a t t 2)) ] <EOF>))"
            },
            {
                /* message */
                "a single named attribute",

                /* input */
                "[att1=value]",

                /* expected */
                "(document (attributeList [ (namedAttribute (attributeName a t t 1) = (attributeValuePart v a l u e)) ] <EOF>))"
            },
            {
                /* message */
                "two named attributes",

                /* input */
                "[att1=value1,att2=value2]",

                /* expected */
                "(document (attributeList [ (namedAttribute (attributeName a t t 1) = (attributeValuePart v a l u e 1)) , (namedAttribute (attributeName a t t 2) = (attributeValuePart v a l u e 2)) ] <EOF>))"
            },
            {
                /* message */
                "two named attributes, space after comma",

                /* input */
                "[att1=value1, att2=value2]",

                /* expected */
                "(document (attributeList [ (namedAttribute (attributeName a t t 1) = (attributeValuePart v a l u e 1)) ,   (namedAttribute (attributeName a t t 2) = (attributeValuePart v a l u e 2)) ] <EOF>))"
            },
            {
                /* message */
                "positional and named attribute",

                /* input */
                "[att1,att2=value2]",

                /* expected */
                "(document (attributeList [ (positionalAttribute (attributeName a t t 1)) , (namedAttribute (attributeName a t t 2) = (attributeValuePart v a l u e 2)) ] <EOF>))"
            },
            {
                /* message */
                "attribute list ended by new line",

                /* input */
                "[att1,att2=value2]\n",

                /* expected */
                "(document (attributeList [ (positionalAttribute (attributeName a t t 1)) , (namedAttribute (attributeName a t t 2) = (attributeValuePart v a l u e 2)) ] \\n))"
            }
        });

    }

}
