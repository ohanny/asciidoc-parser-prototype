package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DocumentTest extends GrammarTest {

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
                "header with author and attribute entry ended by EOI",

                /* input */
                "= A document\n" +
                "John Doe <jd@mail.com>\n" +
                "v1.0\n" +
                "\n" +
                "Some content\n" +
                "\n" +
                "== Section 1\n" +
                "\n" +
                "Other content\n" +
                "\n",

                /* expected */
                "(document (header (documentSection (documentTitle A   d o c u m e n t)) (authors (author (authorName J o h n   D o e) (authorAddress j d @ m a i l . c o m))) (revisionInfo v 1 . 0 \\n)) (bl \\n) (content (preamble (block (paragraph S o m e   c o n t e n t) (nl \\n)) (bl \\n)) (section = =   (sectionTitle S e c t i o n   1) \\n) (bl \\n) (block (paragraph O t h e r   c o n t e n t) (nl \\n)) (bl \\n)) (bl <EOI>))"
            }
        });

    }

}
