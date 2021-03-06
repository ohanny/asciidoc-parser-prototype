package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HeaderWithSingleCommentTest extends GrammarTest {

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
                "header followed by a single line comment",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "// this is a comment",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !)) (singleComment / /   t h i s   i s   a   c o m m e n t <EOI>)))"
            },
            {
                /* message */
                "header followed by a single line comment containing '//' ",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "// this is a comment // is it ok ?",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !)) (singleComment / /   t h i s   i s   a   c o m m e n t   / /   i s   i t   o k   ? <EOI>)))"
            },
            {
                /* message */
                "header followed by a few single lines comment",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "// comment 1\n" +
                "// comment 2\n" +
                "// comment 3",

                /* expected */
                "(document (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !)) (singleComment / /   c o m m e n t   1 \\n) (singleComment / /   c o m m e n t   2 \\n) (singleComment / /   c o m m e n t   3 <EOI>)))"
            },
            {
                /* message */
                "a comment before document title",

                /* input */
                "// This is a comment \n" +
                "= Hello, AsciiDoc!\n",

                /* expected */
                "(document (singleComment / /   T h i s   i s   a   c o m m e n t   \\n) (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (bl <EOI>))"
            },
            {
                /* message */
                "a comment before document title, header containing a paragraph",

                /* input */
                "// This is a comment \n" +
                "= Hello, AsciiDoc!\n" +
                "\n" +
                "Some text...",

                /* expected */
                "(document (singleComment / /   T h i s   i s   a   c o m m e n t   \\n) (header (documentSection (documentTitle H e l l o ,   A s c i i D o c !))) (bl \\n) (content (preamble (block (paragraph S o m e   t e x t . . . <EOI>)))))"
            },

        });

    }

}
