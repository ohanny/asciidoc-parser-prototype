package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SingleCommentTest extends GrammarTest {

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
                "a line comment ended by EOF",

                /* input */
                "// this is a comment",

                /* expected */
                "(document (singleComment / /   t h i s   i s   a   c o m m e n t <EOF>))"
            },
            {
                /* message */
                "a line comment ended by new line",

                /* input */
                "// this is a comment\n",

                /* expected */
                "(document (singleComment / /   t h i s   i s   a   c o m m e n t \\n) (bl <EOF>))"
            },            {
                /* message */
                "a line comment without space after '//' ",

                /* input */
                "//this is a comment",

                /* expected */
                "(document (singleComment / / t h i s   i s   a   c o m m e n t <EOF>))"
            },
            {
                /* message */
                "a few lines comment",

                /* input */
                "// comment 1\n" +
                "// comment 2\n" +
                "// comment 3",

                /* expected */
                "(document (singleComment / /   c o m m e n t   1 \\n) (singleComment / /   c o m m e n t   2 \\n) (singleComment / /   c o m m e n t   3 <EOF>))"
            },
            {
                /* message */
                "a line comment containing '//' ",

                /* input */
                "// this is a comment // is it ok ?",

                /* expected */
                "(document (singleComment / /   t h i s   i s   a   c o m m e n t   / /   i s   i t   o k   ? <EOF>))"
            },
            {
                /* message */
                "a line comment containing '[' and '[[' ",

                /* input */
                "// this is a comment with [ and [[ is it ok ?",

                /* expected */
                "(document (singleComment / /   t h i s   i s   a   c o m m e n t   w i t h   [   a n d   [ [   i s   i t   o k   ? <EOF>))"
            },
            {
                /* message */
                "a line comment containing ']' and ']]' ",

                /* input */
                "// this is a comment with ] and ]] is it ok ?",

                /* expected */
                "(document (singleComment / /   t h i s   i s   a   c o m m e n t   w i t h   ]   a n d   ] ]   i s   i t   o k   ? <EOF>))"
            },
            {
                /* message */
                "a line comment containing '[hello]'",

                /* input */
                "// this is a comment with [hello] is it ok ?",

                /* expected */
                "(document (singleComment / /   t h i s   i s   a   c o m m e n t   w i t h   [ h e l l o ]   i s   i t   o k   ? <EOF>))"
            },
            {
                /* message */
                "a line comment containing '[[hello]]'",

                /* input */
                "// this is a comment with [[hello]] is it ok ?",

                /* expected */
                "(document (singleComment / /   t h i s   i s   a   c o m m e n t   w i t h   [ [ h e l l o ] ]   i s   i t   o k   ? <EOF>))"
            }

        });

    }

}
