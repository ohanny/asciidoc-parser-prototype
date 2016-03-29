package fr.icodem.asciidoc.parser.antlr;

import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;
import fr.icodem.asciidoc.parser.peg.example.AsciidocPegParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public abstract class GrammarTest {

    private final static boolean ANTLR = false;

    protected void check(String message, String input, String expected) {
        if (ANTLR) {
            ParseTree tree = getParseTree(input);
            AsciidocParser parser = new AsciidocParser(null);
            assertEquals(message, expected, tree.toStringTree(parser));
        }
        else {
            AsciidocPegParser parser = new AsciidocPegParser();
            ParsingResult result = new ParseRunner(parser, parser::document)
                    .generateStringTree()
                    //.trace()
                    //.parse(input);
                    .parse(new StringReader(input), null, null, null);

            assertEquals(message, expected, result.tree);
        }
    }

    protected ParserRuleContext getParseTree(String text) {
        // create a parser for Asciidoc grammar
        ANTLRInputStream input = new ANTLRInputStream(text);
        AsciidocLexer lexer = new AsciidocLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AsciidocParser parser = new AsciidocParser(tokens);

        // start parsing and get result in LISP style text form
        ParserRuleContext result = parser.document();

        return result;
    }


}
