package fr.icodem.asciidoc.parser.antlr;

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
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) ] <EOF>)))"
            },
            {
                /* message */
                "two positional attributes",

                /* input */
                "[att1,att2]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) , (positionalAttribute (attributeValue a t t 2)) ] <EOF>)))"
            },
            {
                /* message */
                "two positional attributes, space after comma",

                /* input */
                "[att1, att2]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) ,   (positionalAttribute (attributeValue a t t 2)) ] <EOF>)))"
            },
            {
                /* message */
                "a single named attribute",

                /* input */
                "[att1=value]",

                /* expected */
                "(document (content (attributeList [ (namedAttribute (attributeName a t t 1) = (attributeValue v a l u e)) ] <EOF>)))"
            },
            {
                /* message */
                "two named attributes",

                /* input */
                "[att1=value1,att2=value2]",

                /* expected */
                "(document (content (attributeList [ (namedAttribute (attributeName a t t 1) = (attributeValue v a l u e 1)) , (namedAttribute (attributeName a t t 2) = (attributeValue v a l u e 2)) ] <EOF>)))"
            },
            {
                /* message */
                "two named attributes, space after comma",

                /* input */
                "[att1=value1, att2=value2]",

                /* expected */
                "(document (content (attributeList [ (namedAttribute (attributeName a t t 1) = (attributeValue v a l u e 1)) ,   (namedAttribute (attributeName a t t 2) = (attributeValue v a l u e 2)) ] <EOF>)))"
            },
            {
                /* message */
                "positional and named attribute",

                /* input */
                "[att1,att2=value2]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) , (namedAttribute (attributeName a t t 2) = (attributeValue v a l u e 2)) ] <EOF>)))"
            },
            {
                /* message */
                "attribute list ended by new line",

                /* input */
                "[att1,att2=value2]\n",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) , (namedAttribute (attributeName a t t 2) = (attributeValue v a l u e 2)) ] \\n)) (bl <EOF>))"
            },
            {
                /* message */
                "positional attribute with blank",

                /* input */
                "[Kiwi Orange]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue K i w i   O r a n g e)) ] <EOF>)))"
            },
            {
                /* message */
                "attribute list in section ended by new line",

                /* input */
                "== Section\n" +
                "[att1,att2=value2]\n",

                /* expected */
                "(document (content (section (sectionTitle = =   (title S e c t i o n) \\n) (attributeList [ (positionalAttribute (attributeValue a t t 1)) , (namedAttribute (attributeName a t t 2) = (attributeValue v a l u e 2)) ] \\n))) (bl <EOF>))"
            },
            {
                /* message */
                "attribute list in section ended by EOF",

                /* input */
                "== Section\n" +
                "[att1,att2=value2]",

                /* expected */
                "(document (content (section (sectionTitle = =   (title S e c t i o n) \\n) (attributeList [ (positionalAttribute (attributeValue a t t 1)) , (namedAttribute (attributeName a t t 2) = (attributeValue v a l u e 2)) ] <EOF>))))"
            },
            {
                /* message */
                "attribute list before a paragraph",

                /* input */
                "== Section\n" +
                "\n" +
                "[att1,att2=value2]\n" +
                "A paragraph",

                /* expected */
                "(document (content (section (sectionTitle = =   (title S e c t i o n) \\n) (bl \\n) (attributeList [ (positionalAttribute (attributeValue a t t 1)) , (namedAttribute (attributeName a t t 2) = (attributeValue v a l u e 2)) ] \\n) (block (paragraph A   p a r a g r a p h <EOF>)))))"
            }
        });

    }

}
