package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PreambleTest extends GrammarTest {

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
                "a header with simple preamble",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\n" +
                "XXX\n",

                /* expected */
                "(document (header (documentTitle (title H e l l o ,   A s c i i D o c !))) (bl \\n) (preamble (block (paragraph X X X) (nl \\n))) (bl <EOF>))"
            },
            {
                /* message */
                "A paragraph ended a with '== ' line that should not be a section title",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\n" +
                "XXX\n" +
                "== First Section\n",

                /* expected */
                "(document (header (documentTitle (title H e l l o ,   A s c i i D o c !))) (bl \\n) (preamble (block (paragraph X X X \\n = =   F i r s t   S e c t i o n) (nl \\n))) (bl <EOF>))"
            },
            {
                /* message */
                "a comment between two paragraphs inside header",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\n" +
                "Some text...\n" +
                "\n" +
                "// This is a comment\n" +
                "\n" +
                "Some other text...",

                /* expected */
                "(document (header (documentTitle (title H e l l o ,   A s c i i D o c !))) (bl \\n) (preamble (block (paragraph S o m e   t e x t . . .) (nl \\n)) (bl \\n) (block (singleComment / /   T h i s   i s   a   c o m m e n t \\n)) (bl \\n) (block (paragraph S o m e   o t h e r   t e x t . . . <EOF>))))"
            },
            {
                /* message */
                "a comment between two paragraphs inside header, but no blank line before comment",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\n" +
                "Some text...\n" +
                "// This is a comment\n" +
                "\n" +
                "Some other text...",

                /* expected */
                "(document (header (documentTitle (title H e l l o ,   A s c i i D o c !))) (bl \\n) (preamble (block (paragraph S o m e   t e x t . . . \\n)) (block (singleComment / /   T h i s   i s   a   c o m m e n t \\n)) (bl \\n) (block (paragraph S o m e   o t h e r   t e x t . . . <EOF>))))"
            },
            {
                /* message */
                "a comment between two paragraphs inside header, but no blank line before and after comment",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\n" +
                "Some text...\n" +
                "// This is a comment\n" +
                "Some other text...",

                /* expected */
                "(document (header (documentTitle (title H e l l o ,   A s c i i D o c !))) (bl \\n) (preamble (block (paragraph S o m e   t e x t . . . \\n)) (block (singleComment / /   T h i s   i s   a   c o m m e n t \\n)) (block (paragraph S o m e   o t h e r   t e x t . . . <EOF>))))"
            }

        });

    }

}
