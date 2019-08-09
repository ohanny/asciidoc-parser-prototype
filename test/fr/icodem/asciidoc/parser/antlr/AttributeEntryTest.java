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
                "(document (content (preamble (attributeEntry : (attributeEntryName f r u i t) :   (attributeValueParts (attributeValuePart k i w i)) <EOI>))))"
            },
            {
                /* message */
                "an attribute entry ended by new line",

                /* input */
                ":fruit: kiwi\n",

                /* expected */
                "(document (content (preamble (attributeEntry : (attributeEntryName f r u i t) :   (attributeValueParts (attributeValuePart k i w i)) \\n))) (bl <EOI>))"
            },
            {
                /* message */
                "two attribute entries",

                /* input */
                ":fruit: kiwi\n" +
                ":vegetable: cabbage",

                /* expected */
                "(document (content (preamble (attributeEntry : (attributeEntryName f r u i t) :   (attributeValueParts (attributeValuePart k i w i)) \\n) (attributeEntry : (attributeEntryName v e g e t a b l e) :   (attributeValueParts (attributeValuePart c a b b a g e)) <EOI>))))"
            },
            {
                /* message */
                "two attribute entries, last one ended by new line",

                /* input */
                ":fruit: kiwi\n" +
                ":vegetable: cabbage\n",

                /* expected */
                "(document (content (preamble (attributeEntry : (attributeEntryName f r u i t) :   (attributeValueParts (attributeValuePart k i w i)) \\n) (attributeEntry : (attributeEntryName v e g e t a b l e) :   (attributeValueParts (attributeValuePart c a b b a g e)) \\n))) (bl <EOI>))"
            },
            {
                /* message */
                "attribute entry with no value",

                /* input */
                ":fruit:",

                /* expected */
                "(document (content (preamble (attributeEntry : (attributeEntryName f r u i t) : <EOI>))))"
            },
            {
                /* message */
                "attribute value continued on next line",

                /* input */
                ":fruit: kiwi+\n" +
                "fruit",

                /* expected */
                "(document (content (preamble (attributeEntry : (attributeEntryName f r u i t) :   (attributeValueParts (attributeValuePart k i w i) + \\n (attributeValuePart f r u i t)) <EOI>))))"
            },
            {
                /* message */
                "attribute value continued on next line with leading spaces",

                /* input */
                ":fruit: kiwi+\n" +
                "   fruit",

                /* expected */
                "(document (content (preamble (attributeEntry : (attributeEntryName f r u i t) :   (attributeValueParts (attributeValuePart k i w i) + \\n       (attributeValuePart f r u i t)) <EOI>))))"
            },
            {
                /* message */
                "unset attribute with leading bang",

                /* input */
                ":!fruit:",

                /* expected */
                "(document (content (preamble (attributeEntry : ! (attributeEntryName f r u i t) : <EOI>))))"
            },
            {
                /* message */
                "unset attribute with trailing bang",

                /* input */
                ":fruit!:",

                /* expected */
                "(document (content (preamble (attributeEntry : (attributeEntryName f r u i t) ! : <EOI>))))"
            },
            {
                /* message */
                "attribute entry in section ended by EOF",

                /* input */
                "== Section\n" +
                ":fruit: kiwi",

                /* expected */
                "(document (content (section = =   (sectionTitle S e c t i o n) \\n) (attributeEntry : (attributeEntryName f r u i t) :   (attributeValueParts (attributeValuePart k i w i)) <EOI>)))"
            },
            {
                /* message */
                "attribute entry in section ended by new line",

                /* input */
                "== Section\n" +
                ":fruit: kiwi\n",

                /* expected */
                "(document (content (section = =   (sectionTitle S e c t i o n) \\n) (attributeEntry : (attributeEntryName f r u i t) :   (attributeValueParts (attributeValuePart k i w i)) \\n)) (bl <EOI>))"
            },
            {
                /* message */
                "attribute entry with dash character",

                /* input */
                ":toc-title: Sommaire\n",

                /* expected */
                "(document (content (preamble (attributeEntry : (attributeEntryName t o c - t i t l e) :   (attributeValueParts (attributeValuePart S o m m a i r e)) \\n))) (bl <EOI>))"
            }
        });

    }

}
