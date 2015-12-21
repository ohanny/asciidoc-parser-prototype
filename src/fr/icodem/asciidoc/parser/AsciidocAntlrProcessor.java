package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.antlr.AsciidocLexer;
import fr.icodem.asciidoc.parser.antlr.AsciidocParser;
import fr.icodem.asciidoc.parser.elements.*;
import fr.icodem.asciidoc.parser.elements.AbstractList;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.*;
import java.util.stream.Collectors;

import static fr.icodem.asciidoc.parser.ActionRequest.ActionRequestType.*;
import static java.lang.Math.min;

public class AsciidocAntlrProcessor extends AsciidocProcessor {

    private HeaderContext headerContext;
    private RootListContext rootListContext;
    private String currentTitle;
    private List<Attribute> rawAttList;

    boolean blockNestedInList;

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

    private void notifyDocumentHeaderIfNotDone() {
        if (!headerContext.documentHeaderNotified) {
            DocumentHeader header = headerContext.getHeader();
            boolean ready = headerContext.headerPresent;

            emitter.addActionRequest(DocumentHeader, () -> handler.documentHeader(header), ready);
            headerContext.documentHeaderNotified = true;
        }
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
        headerContext = new HeaderContext();
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
            headerContext.setTitle(ef.title(currentTitle));
            currentTitle = null;
        }
    }

    @Override
    public void exitHeader(AsciidocParser.HeaderContext ctx) {
        headerContext.setHeaderPresent(true);
        notifyDocumentHeaderIfNotDone();
    }

    @Override
    public void enterAuthor(AsciidocParser.AuthorContext ctx) {
        final String address = (ctx.authorAddress() == null)?
                null:ctx.authorAddress().getText();
        Author author = ef.author(null, ctx.authorName().getText().trim(),
                address, headerContext.getNextAuthorPosition());
        headerContext.addAuthor(author);
    }

    @Override
    public void enterAttributeEntry(AsciidocParser.AttributeEntryContext ctx) {
        String value = null;
        if (ctx.attributeValueParts() != null) {
            value = ctx.attributeValueParts().getText();// TODO concat all parts by iteration
        }

        boolean enabled = ctx.BANG().size() > 0;

        AttributeEntry att = ef.attributeEntry(ctx.attributeName().getText(), value, enabled);

        if (!headerContext.documentHeaderNotified) {
            headerContext.addAttribute(att);
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

        if (headerContext.documentTitleUndefined) {
            headerContext.documentTitleUndefined = false;
            headerContext.setTitle(ef.title(currentTitle));

            DocumentHeader header = headerContext.getHeader();
            emitter.markFirstReady(DocumentHeader, () -> handler.documentHeader(header));
            emitter.releaseListener();

        }
        emitter.addActionRequest(StartSectionTitle, () -> handler.startSectionTitle(sectionTitle), true);

        currentTitle = null;

    }

    @Override
    public void enterBlock(AsciidocParser.BlockContext ctx) {
        blockNestedInList = ctx.fromList;
    }

    @Override
    public void enterParagraph(AsciidocParser.ParagraphContext ctx) {
        String text = ctx.getText().trim();
        Paragraph p = ef.paragraph(consumeAttList(), text);
        if (!blockNestedInList) {
            emitter.addActionRequest(StartParagraph, () -> handler.startParagraph(p), true);
        } else {
            rootListContext.addBlockToLastListItem(p);
        }
    }

    @Override
    public void enterList(AsciidocParser.ListContext ctx) {
        rootListContext = new RootListContext();
        rootListContext.enterList();
    }

    private ListItem toListItem(ListItemContext ctx) {
        List<Block> blocks = null;
        if (ctx.blocks == null) {
            blocks = Collections.emptyList();
        }
        else {
            blocks = Collections.unmodifiableList(ctx.blocks);
        }

        ListItem li = null;
        if (ctx.nestedList == null) { // leaf
            li = ef.listItem(null, ctx.text, null, blocks);
        }
        else {
            li = ef.listItem(null, ctx.text, toList(ctx.nestedList), blocks);
        }
        return li;
    }

    private List<ListItem> toListItems(List<ListItemContext> itemsCtx) {
        return itemsCtx.stream()
                .map(ctx -> toListItem(ctx))
                .collect(Collectors.toList());
    }

    private AbstractList toList(ListContext ctx) {
        AbstractList list = null;
        if (ctx.isUnordered()) {
            list = ef.unorderedList(ctx.attributeList, toListItems(ctx.items), ctx.level);
        }
        else if (ctx.isOrdered()) {
            list = ef.orderedList(ctx.attributeList, toListItems(ctx.items), ctx.level);
        }

        return list;
    }

    @Override
    public void exitList(AsciidocParser.ListContext ctx) {
        rootListContext.exitList();

        //
//        rootListContext.flattened()
//                       .map(l -> l.type)
//                       .collect(Collectors.toCollection(LinkedList::new))
//                       .descendingIterator()
//                       .forEachRemaining(System.out::println);

//        List<ListContext> list = rootListContext.flattened()
//                       .collect(Collectors.toCollection(LinkedList::new));
//        Collections.reverse(list);

//        System.out.println("=======================");
        AbstractList list = toList(rootListContext.listContext);
        emitter.addActionRequest(VisitList, () -> handler.visitList(list));
//        System.out.println(l);
//        System.out.println("=======================");


        rootListContext = null;

    }

    @Override
    public void enterListItem(AsciidocParser.ListItemContext ctx) {
        AttributeList attList = consumeAttList();

        rootListContext.addItem(ctx.listItemValue().getText(),
                ctx.TIMES().size(), ctx.DOT().size(), attList);
    }

    @Override
    public void enterTableCell(AsciidocParser.TableCellContext ctx) {
        //ctx.tableCellSpecifiers().CELL_SPECIFIERS().getText();
    }

}
