package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.backend.html.HtmlBackend;
import fr.icodem.asciidoc.parser.antlr.AsciidocLexer;
import fr.icodem.asciidoc.parser.antlr.AsciidocParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.StringWriter;

public class HeaderTest {

    @Test
    public void test() throws Exception {

        String text = "= Hello\n" +
                      "John Doe\n" +
                      ":fruit: kiwi\n" +
                      "\n" +
                      "The sun, the earth and the sea.\n" +
                      "\n" +
                      "== About fruits\n" +
                      "I love fruits\n";

        // create a parser for Asciidoc grammar
        ANTLRInputStream input = new ANTLRInputStream(text);
        AsciidocLexer lexer = new AsciidocLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AsciidocParser parser = new AsciidocParser(tokens);

        // start parsing and get result in LISP style text form
        ParseTree tree = parser.document();

        // Create a generic parse tree walker that can trigger callbacks
        ParseTreeWalker walker = new ParseTreeWalker();

        // Walk the tree created during the parse, trigger callbacks
        StringWriter writer = new StringWriter();
        walker.walk(new AsciidocDocumentProcessor(new HtmlBackend(writer)), tree);

        System.out.println(writer);

    }
}
