package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.antlr.AsciidocLexer;
import fr.icodem.asciidoc.parser.antlr.AsciidocParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AsciidocAntlrProcessor extends AsciidocProcessor {

    public AsciidocAntlrProcessor(AsciidocParserHandler handler) {
        super(handler);
    }

    @Override
    public void parse(String text) {
        // create a parser for Asciidoc grammar
        ANTLRInputStream input = new ANTLRInputStream(text);
        AsciidocLexer lexer = new AsciidocLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AsciidocParser parser = new AsciidocParser(tokens);

        // start parsing
        ParseTree tree = parser.document();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(this, tree);

        System.out.println(tree.toStringTree(parser));//TODO

    }

    @Override
    public void enterDocument(AsciidocParser.DocumentContext ctx) {
        handler.startDocument(ef.document());
    }

    @Override
    public void exitDocument(AsciidocParser.DocumentContext ctx) {
        handler.endDocument(ef.document());
    }

    @Override
    public void enterDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
        handler.startDocumentTitle(ef.documentTitle());
    }

    @Override
    public void exitDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
        handler.endDocumentTitle(ef.documentTitle());
    }

    @Override
    public void enterTitle(AsciidocParser.TitleContext ctx) {
        final String text = ctx.getText();
        handler.startTitle(ef.title(text));
    }

    @Override
    public void enterParagraph(AsciidocParser.ParagraphContext ctx) {
        String text = ctx.getText().trim();
        handler.startParagraph(ef.paragraph(text));
    }

    @Override
    public void enterSection(AsciidocParser.SectionContext ctx) {
        handler.startSection(ef.section());
    }

    @Override
    public void exitSection(AsciidocParser.SectionContext ctx) {
        handler.endSection(ef.section());
    }

    @Override
    public void enterSectionTitle(AsciidocParser.SectionTitleContext ctx) {
        int level = max(min(ctx.EQ().size(), 6), 1);
        handler.startSectionTitle(ef.sectionTitle(level));
    }

    @Override
    public void exitSectionTitle(AsciidocParser.SectionTitleContext ctx) {
        int level = min(ctx.EQ().size(), 6);
        handler.endSectionTitle(ef.sectionTitle(level));
    }

    @Override
    public void enterAuthors(AsciidocParser.AuthorsContext ctx) {
        System.out.println(ctx.getText());
    }

    @Override
    public void enterAttributeEntry(AsciidocParser.AttributeEntryContext ctx) {
        System.out.println("XX"+ctx.getText());
    }

    @Override
    public void exitAttributeEntry(AsciidocParser.AttributeEntryContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode node) {
        //System.out.println(node.getSymbol());
        //System.out.println(node.getText());
    }

}
