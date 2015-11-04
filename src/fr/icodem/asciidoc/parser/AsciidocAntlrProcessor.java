package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.antlr.AsciidocLexer;
import fr.icodem.asciidoc.parser.antlr.AsciidocParser;
import fr.icodem.asciidoc.parser.elements.AttributeEntry;
import fr.icodem.asciidoc.parser.elements.Author;
import fr.icodem.asciidoc.parser.elements.Document;
import fr.icodem.asciidoc.parser.elements.DocumentTitle;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.xml.sax.ContentHandler;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AsciidocAntlrProcessor extends AsciidocProcessor {

    private class ModifiableDocument {
        DocumentTitle title;
        List<Author> authors;
        Map<String, AttributeEntry> nameToAttributeMap;
        boolean headerPresent;

        ModifiableDocument() {
            nameToAttributeMap = AttributeDefaults.Instance.getAttributes();

        }

        void setTitle(DocumentTitle title) {
            this.title = title;
        }

        void addAttribute(AttributeEntry att) {
            this.nameToAttributeMap.put(att.getName(), att);
        }

        void addAuthor(Author author) {
            if (this.authors == null) this.authors = new ArrayList<>();
            this.authors.add(author);
        }

        int getNextAuthorPosition() {
            int position = (authors == null)?1:authors.size() + 1;
            return position;
        }

        void setHeaderPresent(boolean headerPresent) {
            this.headerPresent = headerPresent;
        }

        Document getDocument() {
            return ef.document(title,
                               Collections.unmodifiableList(authors),
                               Collections.unmodifiableMap(nameToAttributeMap),
                               headerPresent);
        }

    }

    private ModifiableDocument document;
    private boolean documentNotified;


    private String currentTitle;

    public AsciidocAntlrProcessor(AsciidocParserHandler handler, List<AttributeEntry> attributes) {
        super(handler, attributes);
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
    }

    @Override
    public void enterDocument(AsciidocParser.DocumentContext ctx) {
        document = new ModifiableDocument();
        //handler.startDocument(ef.document());
    }

    @Override
    public void exitDocument(AsciidocParser.DocumentContext ctx) {
        //handler.endDocument(ef.document());
        handler.endDocument(document.getDocument());
    }

    @Override
    public void enterDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
        currentTitle = null;
        //handler.startDocumentTitle(ef.documentTitle());
    }

    @Override
    public void exitDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
        if (currentTitle != null) {
            document.setTitle(ef.documentTitle(currentTitle));
            currentTitle = null;
        }
        //handler.endDocumentTitle(ef.documentTitle());
    }

    private void notifyDocumentIfNotDone() {
        if (!documentNotified) {
            handler.startDocument(document.getDocument());
            documentNotified = true;
        }
    }

    @Override
    public void exitHeader(AsciidocParser.HeaderContext ctx) {
        document.setHeaderPresent(true);
        notifyDocumentIfNotDone();
    }

    @Override
    public void enterPreamble(AsciidocParser.PreambleContext ctx) {
        handler.startPreamble();
    }

    @Override
    public void exitPreamble(AsciidocParser.PreambleContext ctx) {
        handler.endPreamble();
    }

    @Override
    public void enterContent(AsciidocParser.ContentContext ctx) {
        notifyDocumentIfNotDone();
        handler.startContent();
    }

    @Override
    public void exitContent(AsciidocParser.ContentContext ctx) {
        handler.endContent();
    }

    @Override
    public void enterTitle(AsciidocParser.TitleContext ctx) {
        //final String text = ctx.getText();
        //handler.startTitle(ef.title(text));
        currentTitle = ctx.getText();
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

//    @Override
//    public void enterAuthors(AsciidocParser.AuthorsContext ctx) {
//        authors = new ArrayList<>();
//    }

    @Override
    public void enterAuthor(AsciidocParser.AuthorContext ctx) {
        final String address = (ctx.authorAddress() == null)?
                null:ctx.authorAddress().getText();
        Author author = ef.author(null, ctx.authorName().getText().trim(),
                address, document.getNextAuthorPosition());
        document.addAuthor(author);
    }

//    @Override
//    public void exitAuthors(AsciidocParser.AuthorsContext ctx) {
//        handler.startAuthors(authors);
//        authors = null;
//    }

    @Override
    public void enterAttributeEntry(AsciidocParser.AttributeEntryContext ctx) {
        if (!documentNotified) {
            AttributeEntry att = ef.attributeEntry(ctx.attributeName().getText(), ctx.attributeValue().getText());
            document.addAttribute(att);
        }
        //handler.startAttributeEntry(ef.attributeEntry(ctx.attributeName().getText(), ctx.attributeValue().getText()));
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        //System.out.println(node.getSymbol());
        //System.out.println(node.getText());
    }

}
