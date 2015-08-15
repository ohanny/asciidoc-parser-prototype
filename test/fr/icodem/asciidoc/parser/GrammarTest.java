package fr.icodem.asciidoc.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public abstract class GrammarTest {

    protected void check(String message, String input, String expected) {
        ParseTree tree = getParseTree(input);
        AsciidocParser parser = new AsciidocParser(null);
        assertEquals(message, expected, tree.toStringTree(parser));
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
