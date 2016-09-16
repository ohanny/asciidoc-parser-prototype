package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.*;
import fr.icodem.asciidoc.parser.elements.AbstractList;
import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.BlockRules;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

import static fr.icodem.asciidoc.parser.ActionRequest.ActionRequestType.*;
import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.*;
import static java.lang.Math.min;

public class AsciidocPegProcessor implements ParseTreeListener {

    private FormattedTextPegProcessor textProcessor;

    private BlockRules rules = new BlockRules();

    private HeaderContext headerContext;
    private RootListContext rootListContext;

    private Text currentTitle;
    private List<Attribute> rawAttList;

    boolean blockNestedInList;

    private HandlerNotificationsEmitter emitter;

    protected AsciidocParserHandler handler;
    protected ElementFactory ef;
    protected Map<String, AttributeEntry> attributes;

    private Deque<Text> textObjects;

    public AsciidocPegProcessor(AsciidocParserHandler handler, List<AttributeEntry> attributes) {
        this.handler = handler;
        if (handler == null) throw new IllegalArgumentException("Handler must not be null");
        this.ef = new ElementFactory();

        this.attributes = AttributeDefaults.Instance.getAttributes();
        for (AttributeEntry att : attributes) {
            this.attributes.put(att.getName(), att);
        }

        emitter = new HandlerNotificationsEmitter();
        textObjects = new LinkedList<>();

        rules.useFactory(defaultRulesFactory());
        textProcessor = new FormattedTextPegProcessor();
    }

    @Override
    public void characters(NodeContext context, char[] chars, int startIndex, int endIndex) {
        //System.out.println("CHARACTERS => " + textObjects.size() + " xxx: " + new String(chars));
        final Text text = textObjects.peek();
        text.offer(new String(chars));
        if (text instanceof Text.FormattedText) { // TODO remplacer par isFormattedText ?
            textProcessor.parse((Text.FormattedText) text);
        }
    }

    @Override
    public void enterNode(NodeContext context) {
        //System.out.println("ENTER => " + context.getNodeName() + " : " + textObjects.size());

        switch (context.getNodeName()) {
            case "document" :
                enterDocument();
                break;
            case "title" :
                enterTitle();
                break;
            case "documentTitle" :
                enterDocumentTitle();
                break;
            case "attributeList" :
                enterAttributeList();
                break;
            case "preamble" :
                enterPreamble();
                break;
            case "content" :
                enterContent();
                break;
            case "section" :
                enterSection();
                break;
            case "sectionTitle" :
                enterSectionTitle(context);
                break;
            case "list" :
                enterList();
                break;
            case "listItem" :
                enterListItem(context);
                break;
            case "listItemValue" :
                enterListItemValue();
                break;
            case "author" :
                enterAuthor();
                break;
            case "authorAddress" :
                enterAuthorAddress();
                break;
            case "authorName" :
                enterAuthorName();
                break;
            case "idName" :
                enterIdName();
                break;
            case "roleName" :
                enterRoleName();
                break;
            case "optionName" :
                enterOptionName();
                break;
            case "paragraph" :
                enterParagraph();
                break;
            case "positionalAttribute" :
                enterPositionalAttribute();
                break;
            case "namedAttribute" :
                enterNamedAttribute();
                break;
            case "attributeEntry" :
                enterAttributeEntry(context);
                break;
            case "block" :
                enterBlock(context.getBooleanAttribute("fromList", false));
                break;
            case "nl" :
            case "bl" :
            case "authors" :
            case "listContinuation" :
                textObjects.push(Text.dummy());
                break;
        }
    }

    @Override
    public void exitNode(NodeContext context) {
        switch (context.getNodeName()) {
            case "document" :
                exitDocument();
                break;
            case "documentTitle" :
                exitDocumentTitle();
                break;
            case "header" :
                exitHeader();
                break;
            case "preamble" :
                exitPreamble();
                break;
            case "section" :
                exitSection();
                break;
            case "sectionTitle" :
                exitSectionTitle();
                break;
            case "list" :
                exitList();
                break;
            case "authors" :
            case "authorName" :
            case "authorAddress" :
            case "idName" :
            case "roleName" :
            case "optionName" :
            case "paragraph" :
            case "attributeEntry" :
            //case "attributeName" :
            //case "attributeValue" :
            case "positionalAttribute" :
            case "nl" :
            case "bl" :
            case "listItem" :
            case "listItemValue" :
            case "listContinuation" :
            case "title" :
            case "attributeList" :
                textObjects.pop();
                break;
        }

        //System.out.println("EXIT => " + nodeName + " : " + textObjects.size());
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

    public void parse(String text) {
        ParsingResult result = new ParseRunner(rules, rules::document)
                //.trace()
                .parse(new StringReader(text), this, null, null);
    }

    private void enterDocument() {
        headerContext = new HeaderContext();
        emitter.addActionRequest(StartDocument, () -> handler.startDocument(), true);
    }

    private void exitDocument() {
        emitter.addActionRequest(EndDocument, () -> handler.endDocument(), true);
        emitter.parsingComplete();
    }

    private void enterDocumentTitle() {
        currentTitle = null;
        textObjects.push(Text.dummy());
    }

    private void exitDocumentTitle() {
        if (currentTitle != null) {
            headerContext.setTitle(ef.title(currentTitle.getValue()));
            currentTitle = null;
        }
        textObjects.pop();
    }

    private void exitHeader() {
        headerContext.setHeaderPresent(true);
        notifyDocumentHeaderIfNotDone();
    }

    private void enterAuthor() {
        headerContext.addAuthor2(new AuthorContext());
    }

    private void enterAuthorAddress() {
        AuthorContext author = headerContext.getLastAuthor();
        author.address = Text.empty();
        textObjects.push(author.address);
    }

    private void enterAuthorName() {
        AuthorContext author = headerContext.getLastAuthor();
        author.name = Text.empty();
        textObjects.push(author.name);
    }


    private void enterAttributeEntry(NodeContext ctx) {
        textObjects.push(Text.dummy());
        /*
        String value = null;
        if (ctx.attributeValueParts() != null) {
            value = ctx.attributeValueParts().getText();// TODO concat all parts by iteration
        }

        boolean disabled = ctx.isAttributePresent("disabled");
//        boolean disabled = ctx.BANG().size() > 0;

        AttributeEntry att = ef.attributeEntry(ctx.attributeName().getText(), value, disabled);

        if (!headerContext.documentHeaderNotified) {
            headerContext.addAttribute(att);
        } else {
            emitter.addActionRequest(StartAttributeEntry, () -> handler.startAttributeEntry(att), true);
        }
    */
    }

    private void enterAttributeList() {
        if (rawAttList == null) {
            rawAttList = new ArrayList<>();
        }

        textObjects.push(Text.dummy());
    }

    private void enterPositionalAttribute() {
        //String value = ctx.attributeValue().getText();
        //rawAttList.add(ef.attribute(null, value));

        Text value = Text.empty();
        rawAttList.add(new Attribute((String)null, value));

        textObjects.push(value);
    }

    private void enterIdName() {
        Text text = Text.empty();
        rawAttList.add(new Attribute("id", text));
        textObjects.push(text);
    }

    public void enterRoleName() {
        Text text = Text.empty();
        rawAttList.add(new Attribute("role", text));
        textObjects.push(text);
    }

    private void enterOptionName() {
        Text text = Text.empty();
        rawAttList.add(new Attribute("options", text));
        textObjects.push(text);
    }

    private void enterNamedAttribute() {
//        String name = ctx.attributeName().getText();
//        String value = ctx.attributeValue().getText();
//        rawAttList.add(ef.attribute(name, value));
        Text name = Text.empty();
        Text value = Text.empty();
        rawAttList.add(new Attribute(name, value));

        textObjects.push(value);
        textObjects.push(name);
    }

//    private void enterAttributeName() {
//
//    }
//
//    private void enterAttributeValue() {
//
//    }

    private void enterPreamble() {
        emitter.addActionRequest(StartPreamble, () -> handler.startPreamble(), true);
    }

    private void exitPreamble() {
        emitter.addActionRequest(EndPreamble, () -> handler.endPreamble(), true);
    }

    private void enterContent() {
        notifyDocumentHeaderIfNotDone();
    }

    private void enterTitle() {
        currentTitle = Text.empty();
        textObjects.push(currentTitle);
//        currentTitle = ctx.getText();
    }

    private void enterSection() {
        Section section = ef.section();
        emitter.addActionRequest(StartSection, () -> handler.startSection(section), true);
        emitter.addActionRequest(StartSection, () -> handler.startSection(section), true);
    }

    private void exitSection() {
        Section section = ef.section();
        emitter.addActionRequest(StartSection, () -> handler.endSection(section), true);
    }

    private NodeContext contextSectionTitle;// TODO remove when node context is passed to exit
    private void enterSectionTitle(NodeContext context) {
        currentTitle = null;
        contextSectionTitle = context;
        textObjects.push(Text.dummy());
    }

    public void exitSectionTitle() {
        //int level = min(ctx.EQ().size(), 6);
        NodeContext ctx = contextSectionTitle;
        int level = min(ctx.getIntAttribute("eqs.count", -1), 6);
        SectionTitle sectionTitle = ef.sectionTitle(level, currentTitle.getValue());

        if (headerContext.documentTitleUndefined) {
            headerContext.documentTitleUndefined = false;
            headerContext.setTitle(ef.title(currentTitle.getValue()));

            DocumentHeader header = headerContext.getHeader();
            emitter.markFirstReady(DocumentHeader, () -> handler.documentHeader(header));
            emitter.releaseListener();

        }
        emitter.addActionRequest(StartSectionTitle, () -> handler.startSectionTitle(sectionTitle), true);

        currentTitle = null;

        textObjects.pop();

    }

    private void enterBlock(boolean fromList) {
        blockNestedInList = fromList;
    }

    private void enterParagraph() {
        //Text text = Text.empty();
        Text text = Text.formattedText();
        Paragraph p = new Paragraph(consumeAttList(), text);
        if (!blockNestedInList) {
            emitter.addActionRequest(StartParagraph, () -> handler.startParagraph(p), true);
        } else {
            rootListContext.addBlockToLastListItem(p);
        }

        textObjects.push(text);
    }

    private void enterList() {
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
            li = ef.listItem(null, ctx.text.getValue(), null, blocks);
        }
        else {
            li = ef.listItem(null, ctx.text.getValue(), toList(ctx.nestedList), blocks);
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

    private void exitList() {
        rootListContext.exitList();

        AbstractList list = toList(rootListContext.listContext);
        emitter.addActionRequest(VisitList, () -> handler.visitList(list));

        rootListContext = null;

    }

    private void enterListItem(NodeContext ctx) {
        AttributeList attList = consumeAttList();

        Text text = Text.empty();
        textObjects.push(Text.dummy());

        rootListContext.addItem(text,
                ctx.getIntAttribute("times.count", -1), ctx.getIntAttribute("dots.count", -1), attList);
//        rootListContext.addItem(ctx.listItemValue().getText(),
//                ctx.TIMES().size(), ctx.DOT().size(), attList);
    }

    private void enterListItemValue() {
        textObjects.push(rootListContext.getLastItemText());
    }

    private void enterTableCell() {
        //ctx.tableCellSpecifiers().CELL_SPECIFIERS().getText();
    }

}
