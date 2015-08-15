package fr.icodem.asciidoc.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws IOException {
        // asciidoc file input stream
        InputStream is = Main.class.getResourceAsStream("/test.adoc");

        // create a parser for Asciidoc grammar
        ANTLRInputStream input = new ANTLRInputStream(is);
        AsciidocLexer lexer = new AsciidocLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AsciidocParser parser = new AsciidocParser(tokens);

        // start parsing and get result in LISP style text form
        ParseTree tree = parser.document();

        String result = tree.toStringTree(parser);
        System.out.println(result);

    }
}
