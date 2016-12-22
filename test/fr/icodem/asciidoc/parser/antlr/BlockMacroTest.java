package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BlockMacroTest extends GrammarTest {

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
                "block macro with no target nor attribute",

                /* input */
                "toc::[]",

                /* expected */
                "(document (content (macro (macroName t o c) : : (attributeList [ ] <EOF>))))"
            },
            {
                /* message */
                "block macro with target and no attribute",

                /* input */
                "image::sunset.jpg[]",

                /* expected */
                "(document (content (macro (macroName i m a g e) : : (macroTarget s u n s e t . j p g) (attributeList [ ] <EOF>))))"
            },
            {
                /* message */
                "block macro with target and one attribute",

                /* input */
                "image::sunset.jpg[Sunset]",

                /* expected */
                "(document (content (macro (macroName i m a g e) : : (macroTarget s u n s e t . j p g) (attributeList [ (positionalAttribute (attributeValue S u n s e t)) ] <EOF>))))"
            },
            {
                /* message */
                "block macro surrounded by two paragraphs",

                /* input */
                "Block above\n" +
                "\n" +
                " image::sunset.jpg[Sunset]\n" +
                "\n" +
                "Block below",

                /* expected */
                "(document (content (block (paragraph B l o c k   a b o v e) (nl \\n)) (bl \\n) (macro (macroName   i m a g e) : : (macroTarget s u n s e t . j p g) (attributeList [ (positionalAttribute (attributeValue S u n s e t)) ] \\n)) (bl \\n) (block (paragraph B l o c k   b e l o w <EOF>))))"
            }
        });

    }

}
