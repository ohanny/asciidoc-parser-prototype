package fr.icodem.asciidoc.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class HeaderWithContentTest extends GrammarTest {

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
                "A paragraph preceded by new line in the header",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\nXXX\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) " +
                "(preamble (nl \\n) (paragraph X X X \\n)))" +
                ")"
            },
            {
                /* message */
                "A paragraph ended with new line directly after title in the header",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "XXX\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) " +
                "(preamble (paragraph X X X \\n)))" +
                ")"
            },
            {
                /* message */
                "A paragraph ended with end of file directly after title in the header",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "XXX",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) " +
                "(preamble (paragraph X X X)))" +
                ")"
            },
            {
                /* message */
                "A paragraph ended a with '== ' line that should not be a section title",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "\nXXX\n" +
                "== First Section\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) " +
                "(preamble (nl \\n) (paragraph X X X \\n = =   F i r s t   S e c t i o n \\n)))" +
                ")"
            },
            {
                /* message */
                "A paragraph directly after title in the header, and ended a with '== ' line that should not be a section title",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "XXX\n" +
                "== First Section\n",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) " +
                "(preamble (paragraph X X X \\n = =   F i r s t   S e c t i o n \\n)))" +
                ")"
            },
            {
                /* message */
                "A paragraph directly after title in the header, containing a '== ' line that should not be a section title",

                /* input */
                "= Hello, AsciiDoc!\n" +
                "XXX\n" +
                "== First Section\n" +
                "YYY",

                /* expected */
                "(document (header (documentTitle =   (title H e l l o ,   A s c i i D o c !) \\n) " +
                "(preamble (paragraph X X X \\n = =   F i r s t   S e c t i o n \\n Y Y Y)))" +
                ")"
            }

        });

    }

}
