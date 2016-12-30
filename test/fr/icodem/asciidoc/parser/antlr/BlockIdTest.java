package fr.icodem.asciidoc.parser.antlr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BlockIdTest extends GrammarTest {

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
                "a block ID above a paragraph",

                /* input */
                "[[TheID]]\n" +
                "This is some content",

                /* expected */
                "(document (content (anchor [ [ (anchorId T h e I D) ] ] \\n) (block (paragraph T h i s   i s   s o m e   c o n t e n t <EOI>))))"
            },
            {
                /* message */
                "a block ID with a label above a paragraph",

                /* input */
                "[[TheID, The label]]\n" +
                "This is some content",

                /* expected */
                "(document (content (anchor [ [ (anchorId T h e I D) , (anchorLabel   T h e   l a b e l) ] ] \\n) (block (paragraph T h i s   i s   s o m e   c o n t e n t <EOI>))))"
            },
            {
                /* message */
                "two block IDs above a paragraph",

                /* input */
                "[[TheID, The label]]\n" +
                "[[TheID2, The label]]\n" +
                "This is some content",

                /* expected */
                "(document (content (anchor [ [ (anchorId T h e I D) , (anchorLabel   T h e   l a b e l) ] ] \\n) (anchor [ [ (anchorId T h e I D 2) , (anchorLabel   T h e   l a b e l) ] ] \\n) (block (paragraph T h i s   i s   s o m e   c o n t e n t <EOI>))))"
            }
        });

    }

}
