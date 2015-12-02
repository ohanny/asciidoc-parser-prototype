package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.antlr.AsciidocLexer;
import fr.icodem.asciidoc.parser.antlr.AsciidocParser;
import fr.icodem.asciidoc.parser.elements.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static fr.icodem.asciidoc.parser.ActionRequest.ActionRequestType.*;
import static java.lang.Math.min;

public class AsciidocAntlrProcessor extends AsciidocProcessor {

    private class HeaderInfoHolder {
        Title title;
        List<Author> authors;
        Map<String, AttributeEntry> nameToAttributeMap;
        boolean headerPresent;
        boolean documentTitleUndefined = true;
        boolean documentHeaderNotified;

        HeaderInfoHolder() {
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
            return (authors == null)?1:authors.size() + 1;
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

    private HeaderInfoHolder headerInfo;
    private String currentTitle;
    private List<Attribute> rawAttList;

    private HandlerNotificationsEmitter emitter;

    public AsciidocAntlrProcessor(AsciidocParserHandler handler, List<AttributeEntry> attributes) {
        super(handler, attributes);

        emitter = new HandlerNotificationsEmitter();
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
        headerInfo = new HeaderInfoHolder();
        emitter.addActionRequest(StartDocument, () -> handler.startDocument(), true);
    }

    @Override
    public void exitDocument(AsciidocParser.DocumentContext ctx) {
        emitter.addActionRequest(EndDocument, () -> handler.endDocument(), true);
        emitter.parsingComplete();
    }

    @Override
    public void enterDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
        currentTitle = null;
    }

    @Override
    public void exitDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
        if (currentTitle != null) {
            headerInfo.setTitle(ef.title(currentTitle));
            currentTitle = null;
        }
    }

    private void notifyDocumentHeaderIfNotDone() {
        if (!headerInfo.documentHeaderNotified) {
            DocumentHeader header = headerInfo.getHeader();
            boolean ready = headerInfo.headerPresent;

            emitter.addActionRequest(DocumentHeader, () -> handler.documentHeader(header), ready);
            headerInfo.documentHeaderNotified = true;
        }
    }

    @Override
    public void exitHeader(AsciidocParser.HeaderContext ctx) {
        headerInfo.setHeaderPresent(true);
        notifyDocumentHeaderIfNotDone();
    }

    @Override
    public void enterPreamble(AsciidocParser.PreambleContext ctx) {
        emitter.addActionRequest(StartPreamble, () -> handler.startPreamble(), true);
    }

    @Override
    public void exitPreamble(AsciidocParser.PreambleContext ctx) {
        emitter.addActionRequest(EndPreamble, () -> handler.endPreamble(), true);
    }

    @Override
    public void enterContent(AsciidocParser.ContentContext ctx) {
        notifyDocumentHeaderIfNotDone();
    }

    @Override
    public void enterTitle(AsciidocParser.TitleContext ctx) {
        currentTitle = ctx.getText();
    }


    @Override
    public void enterParagraph(AsciidocParser.ParagraphContext ctx) {
        String text = ctx.getText().trim();
        Paragraph p = ef.paragraph(consumeAttList(), text);
        emitter.addActionRequest(StartParagraph, () -> handler.startParagraph(p), true);
    }

    @Override
    public void enterSection(AsciidocParser.SectionContext ctx) {
        Section section = ef.section();
        emitter.addActionRequest(StartSection, () -> handler.startSection(section), true);
    }

    @Override
    public void exitSection(AsciidocParser.SectionContext ctx) {
        Section section = ef.section();
        emitter.addActionRequest(StartSection, () -> handler.endSection(section), true);
    }

    @Override
    public void enterSectionTitle(AsciidocParser.SectionTitleContext ctx) {
        currentTitle = null;
    }

    @Override
    public void exitSectionTitle(AsciidocParser.SectionTitleContext ctx) {
        int level = min(ctx.EQ().size(), 6);
        SectionTitle sectionTitle = ef.sectionTitle(level, currentTitle);

        if (headerInfo.documentTitleUndefined) {
            headerInfo.documentTitleUndefined = false;
            headerInfo.setTitle(ef.title(currentTitle));

            DocumentHeader header = headerInfo.getHeader();
            emitter.markFirstReady(DocumentHeader, () -> handler.documentHeader(header));
            emitter.releaseBackend();

        }
        emitter.addActionRequest(StartSectionTitle, () -> handler.startSectionTitle(sectionTitle), true);

        currentTitle = null;

    }

    @Override
    public void enterAuthor(AsciidocParser.AuthorContext ctx) {
        final String address = (ctx.authorAddress() == null)?
                null:ctx.authorAddress().getText();
        Author author = ef.author(null, ctx.authorName().getText().trim(),
                address, headerInfo.getNextAuthorPosition());
        headerInfo.addAuthor(author);
    }

    @Override
    public void enterAttributeEntry(AsciidocParser.AttributeEntryContext ctx) {
        String value = null;
        if (ctx.attributeValueParts() != null) {
            value = ctx.attributeValueParts().getText();
        }

        boolean enabled = ctx.BANG().size() > 0;

        AttributeEntry att = ef.attributeEntry(ctx.attributeName().getText(), value, enabled);

        if (!headerInfo.documentHeaderNotified) {
            headerInfo.addAttribute(att);
        } else {
            emitter.addActionRequest(StartAttributeEntry, () -> handler.startAttributeEntry(att), true);
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
