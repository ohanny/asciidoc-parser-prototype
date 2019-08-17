package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2.AsciidocHandler2;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2.BlockListener2;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.rules2.BlockRules2;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.Reader;
import java.io.StringReader;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;
import static java.lang.Math.min;

public class DocumentModelBuilder implements AsciidocHandler2 {
    private BlockRules2 rules;

    private BuildState state;

    // builders
    private DocumentBuilder documentBuilder;
    private HeaderBuilder headerBuilder;
    private ContentBuilder contentBuilder;

    private AttributeEntryBuilder attributeEntryBuilder;
    private AttributeListBuilder attributeListBuilder;
    private BlockMacroBuilder blockMacroBuilder;

    public static DocumentModelBuilder newDocumentBuilder(AttributeEntries attributeEntries) {
        BuildState state = BuildState.newInstance(attributeEntries);

        DocumentModelBuilder builder = new DocumentModelBuilder();
        builder.rules = new BlockRules2(attributeEntries);
        builder.rules.withFactory(defaultRulesFactory());
        builder.attributeListBuilder = AttributeListBuilder.newBuilder();

        builder.state = state;

        return builder;
    }

    public Document build(String text) {
        final BlockListener2 listener = new BlockListener2(this, state.getAttributeEntries());

        ParsingResult result = new ParseRunner(rules, rules::document)
                //.trace()
                .parse(new StringReader(text), listener, null, null);

        return documentBuilder==null?null:documentBuilder.build();
    }

    public Document build(Reader reader) {
        final BlockListener2 listener = new BlockListener2(this, state.getAttributeEntries());

        ParsingResult result = new ParseRunner(rules, rules::document)
                //.trace()
                .parse(reader, listener, null, null);

        return documentBuilder==null?null:documentBuilder.build();
    }

    // attribute entries
    @Override
    public void enterAttributeEntry() {
        attributeEntryBuilder.clear();
    }

    @Override
    public void attributeEntry(String delim) {
        if (delim.contains("!")) {
            attributeEntryBuilder.setDisabled(true);
        }
    }

    @Override
    public void exitAttributeEntry() {
        AttributeEntry att = attributeEntryBuilder.build();
        state.getAttributeEntries().addAttribute(att);
    }

    @Override
    public void attributeEntryName(String name) {
        attributeEntryBuilder.setName(name);
    }

    @Override
    public void attributeEntryValuePart(String value) {
        if (attributeEntryBuilder.getValue() != null) {
            value = attributeEntryBuilder.getValue() + value;
        }
        attributeEntryBuilder.setValue(value);
    }

    @Override
    public void attributeName(String name) {
        attributeListBuilder.setAttributeName(name);
    }

    @Override
    public void attributeValue(String value) {
        attributeListBuilder.setAttributeValue(value);
    }

    @Override
    public void enterIdAttribute() {
        attributeListBuilder.addIdAttribute();
    }

    @Override
    public void enterRoleAttribute() {
        attributeListBuilder.addRoleAttribute();
    }

    @Override
    public void enterOptionAttribute() {
        attributeListBuilder.addOptionAttribute();
    }

    @Override
    public void enterPositionalAttribute() {
        attributeListBuilder.addPositionalAttribute();
    }

    @Override
    public void enterNamedAttribute() {
        attributeListBuilder.addNamedAttribute();
    }


    // macro
    @Override
    public void enterMacro() {
        blockMacroBuilder = BlockMacroBuilder.of(state.getAttributeEntries(), attributeListBuilder.consume(), state.consumeBlockTitle());
    }

    @Override
    public void exitMacro() {
        attributeListBuilder.consume(); // TODO add attributes to macro
        if (blockMacroBuilder.isBlock()) {
            state.pushBlockToContainer(blockMacroBuilder);
        }
        blockMacroBuilder = null;
    }

    @Override
    public void macroName(String name) {
        blockMacroBuilder.setName(name);
    }

    @Override
    public void macroTarget(String target) {
        blockMacroBuilder.setTarget(target);
    }

    // text
    @Override
    public void formattedText(char[] chars) {
        state.pushText(new String(chars));
    }

    // document
    @Override
    public void enterDocument() {
        attributeEntryBuilder = AttributeEntryBuilder.newBuilder();
        documentBuilder = DocumentBuilder.newBuilder(state);
    }

    @Override
    public void exitDocument() {
        //checkExitSection(-1); TODO OLIV
    }

    // header
    @Override
    public void enterHeader() {
        headerBuilder = documentBuilder.addHeader();
    }

    @Override
    public void exitHeader() {

    }

    @Override
    public void documentTitle(String text) {
        documentBuilder.setTitle(text);
    }

    // authors
    public void enterAuthors() {
    }

    @Override
    public void exitAuthors() {
    }

    @Override
    public void authorName(String name) {
        headerBuilder.setAuthorName(name);
    }

    @Override
    public void authorAddress(String email) {
        headerBuilder.setAuthorEmail(email);
    }

    @Override
    public void exitAuthor() {
        headerBuilder.closeAuthor();
    }

    // revision info
    @Override
    public void enterRevisionInfo() {
        headerBuilder.addRevisionInfo();
    }

    @Override @Deprecated // TODO à revoir pour décomposer en date, number, remark
    public void revisionInfo(String line) {
        headerBuilder.setRevisionInfoDate(line);
    }

    @Override
    public void exitRevisionInfo() {
    }

    // block title
    @Override
    public void blockTitleValue(char[] chars) {// TODO not yet tested
        state.setCurrentBlockTitle(chars);
    }

    // content
    @Override
    public void enterContent() {
        contentBuilder = documentBuilder.addContent();
    }

    @Override
    public void exitContent() {
        contentBuilder.closeContent();
    }

    // preamble
    @Override
    public void enterPreamble() {
        contentBuilder.addPreamble();
    }

    @Override
    public void exitPreamble() {
        contentBuilder.closePreamble();
    }

    // section
    @Override
    public void enterSection(NodeContext context) {
        int level = min(context.getIntAttribute("level", -1), 6);
        contentBuilder.newSection(level, attributeListBuilder.consume());
    }

    @Override
    public void sectionTitle(String title) {
        contentBuilder.setSectionTitle(title);
    }

    // paragraph
    @Override
    public void enterParagraph(String admonition) {
        admonition = admonition == null?null:admonition.toLowerCase();
        AttributeList attList = attributeListBuilder.consume();

        if (attList != null && "quote".equals(attList.getFirstPositionalAttribute())) {
            String attribution = attList.getSecondPositionalAttribute();
            String citationTitle = attList.getThirdPositionalAttribute();
            QuoteBuilder builder = QuoteBuilder.of(attList, state.consumeBlockTitle(), attribution, citationTitle);
            state.pushBlock(builder);
            state.pushTextContainer(builder);
        } else {
            ParagraphBuilder builder = ParagraphBuilder.of(attList, state.consumeBlockTitle(), admonition);
            state.pushBlock(builder);
            state.pushTextContainer(builder);
        }
    }

    @Override
    public void exitParagraph() {
        BlockBuilder block = state.popBlock();

        state.popTextContainer();
        state.pushBlockToContainer(block);
    }

    // list block
    @Override
    public void enterList() {
        ListBlockBuilder builder = ListBlockBuilder.root(state.consumeBlockTitle());
        state.pushBlock(builder);
    }

    @Override
    public void exitList() {
        ListBlockBuilder builder = state.popBlock();
        state.pushBlockToContainer(builder);
    }

    @Override
    public void enterListItem(NodeContext context) {
        int times = context.getIntAttribute("times.count", -1);
        int dots = context.getIntAttribute("dots.count", -1);

        ListBlockBuilder parentBuilder = state.peekBlock();
        ListItemBuilder builder = parentBuilder.newListItem(times, dots, attributeListBuilder.consume());

        state.pushTextContainer(builder);
        state.pushBlockContainer(builder);
    }


    @Override
    public void exitListItem() {
        //handler.endListItem(currentList.level);
        state.popTextContainer();
        state.popBlockContainer();
    }

//    @Override
//    public void enterListItemValue() {
//        //handler.startListItemValue();
//    }
//
//    @Override
//    public void exitListItemValue() {
//        //handler.endListItemValue();
//    }

    // description list
    @Override
    public void enterDescriptionList() {
        DescriptionListBuilder builder = DescriptionListBuilder.newBuilder(attributeListBuilder.consume(), state.consumeBlockTitle());
        state.pushBlock(builder);
        state.pushBlockToContainer(builder);
    }

    @Override
    public void exitDescriptionList() {
        state.popBlock();
    }

    @Override
    public void enterDescriptionListItem() {
        DescriptionListBuilder builder = state.peekBlock();
        builder.newItem();
    }

    @Override
    public void exitDescriptionListItem() {

    }

    @Override
    public void enterDescriptionListItemTitle() {
        DescriptionListBuilder builder = state.peekBlock();
        this.state.pushTextContainer(builder::setItemTitle);
    }

    @Override
    public void exitDescriptionListItemTitle() {
        state.popTextContainer();
    }


    @Override
    public void enterDescriptionListItemContent() {

    }

    @Override
    public void exitDescriptionListItemContent() {

    }

    @Override
    public void enterDescriptionListItemSimpleContent() {
        DescriptionListBuilder builder = state.peekBlock();
        state.pushTextContainer(builder::setItemContent);
    }

    @Override
    public void exitDescriptionListItemSimpleContent() {
        state.popTextContainer();
    }

    // literal block
    @Override
    public void enterLiteralBlock() {
        LiteralBlockBuilder builder = LiteralBlockBuilder.newBuilder(attributeListBuilder.consume(), state.consumeBlockTitle());
        state.pushBlock(builder);
        state.pushTextContainer(builder);
    }

    @Override
    public void exitLiteralBlock() {
        LiteralBlockBuilder builder = state.popBlock();
        state.popTextContainer();
        state.pushBlockToContainer(builder);
    }

    // example block
    @Override
    public void enterExample() {
        ExampleBlockBuilder builder = ExampleBlockBuilder.newBuilder(attributeListBuilder.consume(), state.consumeBlockTitle());
        state.pushBlock(builder);
        state.pushBlockContainer(builder);
    }

    @Override
    public void exitExample() {
        ExampleBlockBuilder builder = state.popBlock();
        state.popBlockContainer();
        state.pushBlockToContainer(builder);
    }

    // sidebar
    @Override
    public void enterSidebar() {
        SidebarBuilder builder = SidebarBuilder.newBuilder(attributeListBuilder.consume(), state.consumeBlockTitle());
        state.pushBlock(builder);
        state.pushBlockContainer(builder);
    }

    @Override
    public void exitSidebar() {
        SidebarBuilder builder = state.peekBlock();
        state.popBlockContainer();
        state.pushBlockToContainer(builder);
    }

    // listing block
    @Override
    public void enterListingBlock() {
        ListingBlockBuilder builder = ListingBlockBuilder.newBuilder(attributeListBuilder.consume(), state.consumeBlockTitle());
        state.pushBlock(builder);
        state.pushTextContainer(builder);
    }

    @Override
    public void exitListingBlock() {
        ListingBlockBuilder builder = state.popBlock();
        state.popTextContainer();
        state.pushBlockToContainer(builder);
    }

    // callouts
    @Override
    public void enterCallouts() {
        ListingBlockBuilder builder = state.peekBlock();
        builder.newCallouts();
    }

    @Override
    public void enterCallout() {
        ListingBlockBuilder builder = state.peekBlock();
        state.pushTextContainer(builder.addCallout());
    }

    @Override
    public void exitCallout() {
        state.popTextContainer();
    }

    @Override
    public void calloutNumber(String nb) {
        ListingBlockBuilder builder = state.peekBlock();
        builder.setCalloutNumber(nb);
    }

    // horizontal rule
    @Override
    public void horizontalRule() {
        state.pushBlockToContainer(HorizontalRuleBuilder.newBuilder());
    }

    // table
    @Override
    public void enterTable(int lineNumber) {
        TableBuilder builder = TableBuilder.newBuilder(attributeListBuilder.consume(), state.consumeBlockTitle(), lineNumber);

        state.pushBlock(builder);
        state.pushBlockToContainer(builder);
    }

    @Override
    public void exitTable() {
        TableBuilder builder = state.popBlock();
        builder.tableEnd();
    }

    @Override
    public void enterTableCell(int lineNumber) {
        TableBuilder builder = state.peekBlock();
        builder.addCell(lineNumber);
    }

    @Override
    public void exitTableCell() {

    }

    @Override
    public void tableBlock(String text) {
        TableBuilder builder = state.peekBlock();
        builder.setContent(text == null ? null : text.trim());
    }
}
