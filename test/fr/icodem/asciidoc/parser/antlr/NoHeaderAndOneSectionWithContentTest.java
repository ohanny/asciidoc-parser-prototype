package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NoHeaderAndOneSectionWithContentTest extends GrammarTest {

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
                "No header and a section with two paragraphs",

                /* input */
                "== First Section\n" +
                "\n" +
                "ZZZ\n" +
                "\n" +
                "YYY",

                /* expected */
                "(document (content (section (sectionTitle = =   (sectionTitleValue F i r s t   S e c t i o n) \\n) (bl \\n) (block (paragraph Z Z Z) (nl \\n)) (bl \\n) (block (paragraph Y Y Y <EOF>)))))"
            },
            {
                /* message */
                "No header, a new line and a section with two paragraphs",

                /* input */
                "\n" +
                "== First Section\n" +
                "\n" +
                "ZZZ\n" +
                "\n" +
                "YYY",

                /* expected */
                "(document (bl \\n) (content (section (sectionTitle = =   (sectionTitleValue F i r s t   S e c t i o n) \\n) (bl \\n) (block (paragraph Z Z Z) (nl \\n)) (bl \\n) (block (paragraph Y Y Y <EOF>)))))"
            },
            {
                /* message */
                "No header, a few new lines and a section with two paragraphs",

                /* input */
                "\n" +
                "\n" +
                "\n" +
                "== First Section\n" +
                "\n" +
                "ZZZ\n" +
                "\n" +
                "YYY",

                /* expected */
                "(document (bl \\n) (bl \\n) (bl \\n) (content (section (sectionTitle = =   (sectionTitleValue F i r s t   S e c t i o n) \\n) (bl \\n) (block (paragraph Z Z Z) (nl \\n)) (bl \\n) (block (paragraph Y Y Y <EOF>)))))"
            },
            {
                /* message */
                "No header, a paragraph and a section with two paragraphs",

                /* input */
                "XXX\n" +
                "\n" +
                "== First Section\n" +
                "\n" +
                "ZZZ\n" +
                "\n" +
                "YYY",

                /* expected */
                "(document (content (block (paragraph X X X) (nl \\n)) (bl \\n) (section (sectionTitle = =   (sectionTitleValue F i r s t   S e c t i o n) \\n) (bl \\n) (block (paragraph Z Z Z) (nl \\n)) (bl \\n) (block (paragraph Y Y Y <EOF>)))))"
            }


        });

    }

}
