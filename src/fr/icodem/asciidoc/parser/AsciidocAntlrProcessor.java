package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.antlr.AsciidocLexer;
import fr.icodem.asciidoc.parser.antlr.AsciidocParser;
import fr.icodem.asciidoc.parser.elements.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AsciidocAntlrProcessor extends AsciidocProcessor {

    private class ModifiableDocument {
        Title title;
        List<Author> authors;
        Map<String, AttributeEntry> nameToAttributeMap;
        boolean headerPresent;

        ModifiableDocument() {
            authors = new ArrayList<>();
            nameToAttributeMap = AttributeDefaults.Instance.getAttributes();
        }

        void setTitle(Title title) {
            this.title = title;
        }

        void addAttribute(AttributeEntry att) {
            this.nameToAttributeMap.put(att.getName(), att);
        }

        void addAuthor(Author author) {
            this.authors.add(author);
        }

        int getNextAuthorPosition() {
            int position = (authors == null)?1:authors.size() + 1;
            return position;
        }

        void setHeaderPresent(boolean headerPresent) {
            this.headerPresent = headerPresent;
        }

        DocumentHeader getHeader() {
            Title title = (this.title == null)?null:ef.title(this.title.getText());

            return ef.documentHeader(title,
                               Collections.unmodifiableList(authors),
                               Collections.unmodifiableMap(nameToAttributeMap),
                               headerPresent);
        }

    }

    private ModifiableDocument document;
    private boolean documentNotified;

    private String currentTitle;

    private List<Attribute> rawAttList;

    public AsciidocAntlrProcessor(AsciidocParserHandler handler, List<AttributeEntry> attributes) {
        super(handler, attributes);
    }

    private AttributeList consumeAttList() {
        AttributeList attList = ef.attributeList(rawAttList);
        rawAttList = null;// consumed

        return attList;
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
        handler.startDocument();
    }

    @Override
    public void exitDocument(AsciidocParser.DocumentContext ctx) {
        handler.endDocument();
    }

    @Override
    public void enterDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
        currentTitle = null;
    }

    @Override
    public void exitDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
        if (currentTitle != null) {
            document.setTitle(ef.title(currentTitle));
            currentTitle = null;
        }
    }

    private void notifyDocumentIfNotDone() {
        if (!documentNotified) {
            handler.documentHeader(document.getHeader());
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
    }

    @Override
    public void enterTitle(AsciidocParser.TitleContext ctx) {
        currentTitle = ctx.getText();
    }


    @Override
    public void enterParagraph(AsciidocParser.ParagraphContext ctx) {
        String text = ctx.getText().trim();
        handler.startParagraph(ef.paragraph(consumeAttList(), text));
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
        currentTitle = null;
    }

    @Override
    public void exitSectionTitle(AsciidocParser.SectionTitleContext ctx) {
        int level = min(ctx.EQ().size(), 6);
        handler.startSectionTitle(ef.sectionTitle(level, currentTitle));
        currentTitle = null;

    }

    @Override
    public void enterAuthor(AsciidocParser.AuthorContext ctx) {
        final String address = (ctx.authorAddress() == null)?
                null:ctx.authorAddress().getText();
        Author author = ef.author(null, ctx.authorName().getText().trim(),
                address, document.getNextAuthorPosition());
        document.addAuthor(author);
    }

    @Override
    public void enterAttributeEntry(AsciidocParser.AttributeEntryContext ctx) {
        String value = null;
        if (ctx.attributeValueParts() != null) {
            value = ctx.attributeValueParts().getText();
        }

        boolean enabled = ctx.BANG().size() > 0;

        AttributeEntry att = ef.attributeEntry(ctx.attributeName().getText(), value, enabled);

        if (!documentNotified) {
            document.addAttribute(att);
        } else {
            handler.startAttributeEntry(att);
        }
    }

    @Override
    public void enterAttributeList(AsciidocParser.AttributeListContext ctx) {
        if (rawAttList == null) {
            rawAttList = new ArrayList<>();
        }
    }

    @Override
    public void enterPositionalAttribute(AsciidocParser.PositionalAttributeContext ctx) {
        String value = ctx.attributeValue().getText();
        rawAttList.add(ef.attribute(null, value));
    }

    @Override
    public void enterIdName(AsciidocParser.IdNameContext ctx) {
        rawAttList.add(ef.attribute("id", ctx.getText()));
    }

    @Override
    public void enterRoleName(AsciidocParser.RoleNameContext ctx) {
        rawAttList.add(ef.attribute("role", ctx.getText()));
    }

    @Override
    public void enterOptionName(AsciidocParser.OptionNameContext ctx) {
        rawAttList.add(ef.attribute("options", ctx.getText()));
    }

    @Override
    public void enterNamedAttribute(AsciidocParser.NamedAttributeContext ctx) {
        String name = ctx.attributeName().getText();
        String value = ctx.attributeValue().getText();
        rawAttList.add(ef.attribute(name, value));
    }
}
