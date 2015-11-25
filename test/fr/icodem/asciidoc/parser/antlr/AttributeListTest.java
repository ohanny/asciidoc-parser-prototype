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
            },
            {
                /* message */
                "positional attribute with an id attribute",

                /* input */
                "[att1#tiger]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) (idAttribute # (idName t i g e r)) ] <EOF>)))"
            },
            {
                /* message */
                "id attribute only",

                /* input */
                "[#tiger]",

                /* expected */
                "(document (content (attributeList [ (idAttribute # (idName t i g e r)) ] <EOF>)))"
            },
            {
                /* message */
                "positional attribute with role attribute",

                /* input */
                "[att1.summary]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) (roleAttribute . (roleName s u m m a r y)) ] <EOF>)))"
            },
            {
                /* message */
                "positional attribute and two role attributes",

                /* input */
                "[att1.summary.incremental]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) (roleAttribute . (roleName s u m m a r y)) (roleAttribute . (roleName i n c r e m e n t a l)) ] <EOF>)))"
            },
            {
                /* message */
                "two role attributes",

                /* input */
                "[.summary.incremental]",

                /* expected */
                "(document (content (attributeList [ (roleAttribute . (roleName s u m m a r y)) (roleAttribute . (roleName i n c r e m e n t a l)) ] <EOF>)))"
            },
            {
                /* message */
                "id attribute and two role attributes",

                /* input */
                "[#tiger.summary.incremental]",

                /* expected */
                "(document (content (attributeList [ (idAttribute # (idName t i g e r)) (roleAttribute . (roleName s u m m a r y)) (roleAttribute . (roleName i n c r e m e n t a l)) ] <EOF>)))"
            },
            {
                /* message */
                "positional attribute, id attribute and two role attributes",

                /* input */
                "[att1#tiger.summary.incremental]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) (idAttribute # (idName t i g e r)) (roleAttribute . (roleName s u m m a r y)) (roleAttribute . (roleName i n c r e m e n t a l)) ] <EOF>)))"
            },
            {
                /* message */
                "positional attribute and an option attribute",

                /* input */
                "[att1%header]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) (optionAttribute % (optionName h e a d e r)) ] <EOF>)))"
            },
            {
                /* message */
                "positional attribute and two option attributes",

                /* input */
                "[att1%header%footer]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) (optionAttribute % (optionName h e a d e r)) (optionAttribute % (optionName f o o t e r)) ] <EOF>)))"
            },
            {
                /* message */
                "two option attributes",

                /* input */
                "[%header%footer]",

                /* expected */
                "(document (content (attributeList [ (optionAttribute % (optionName h e a d e r)) (optionAttribute % (optionName f o o t e r)) ] <EOF>)))"
            },
            {
                /* message */
                "id attribute and two option attributes",

                /* input */
                "[#tiger%header%footer]",

                /* expected */
                "(document (content (attributeList [ (idAttribute # (idName t i g e r)) (optionAttribute % (optionName h e a d e r)) (optionAttribute % (optionName f o o t e r)) ] <EOF>)))"
            },
            {
                /* message */
                "positional attribute with id attribute and two option attributes",

                /* input */
                "[att1#tiger%header%footer]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) (idAttribute # (idName t i g e r)) (optionAttribute % (optionName h e a d e r)) (optionAttribute % (optionName f o o t e r)) ] <EOF>)))"
            },
            {
                /* message */
                "positional attribute with id attribute, two role attributes and two option attributes",

                /* input */
                "[att1#tiger.summary.incremental%header%footer]",

                /* expected */
                "(document (content (attributeList [ (positionalAttribute (attributeValue a t t 1)) (idAttribute # (idName t i g e r)) (roleAttribute . (roleName s u m m a r y)) (roleAttribute . (roleName i n c r e m e n t a l)) (optionAttribute % (optionName h e a d e r)) (optionAttribute % (optionName f o o t e r)) ] <EOF>)))"
            },
            {
                /* message */
                "id attribute, two role attributes and two option attributes",

                /* input */
                "[#tiger.summary.incremental%header%footer]",

                /* expected */
                "(document (content (attributeList [ (idAttribute # (idName t i g e r)) (roleAttribute . (roleName s u m m a r y)) (roleAttribute . (roleName i n c r e m e n t a l)) (optionAttribute % (optionName h e a d e r)) (optionAttribute % (optionName f o o t e r)) ] <EOF>)))"
            }
        });

    }

}
