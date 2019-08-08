package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2.AsciidocHandler2;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2.BlockListener2;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.rules2.BlockRules2;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;
import static java.lang.Math.min;

public class DocumentModelBuilder implements AsciidocHandler2 {
    private BlockRules2 rules;

    private BuildState state;

    // builders
    private DocumentBuilder documentBuilder;

    private AttributeEntryBuilder attributeEntryBuilder;
    private AttributeListBuilder attributeListBuilder;

    private ParagraphBuilder paragraphBuilder;
    private QuoteBuilder quoteBuilder;
    private ListBlockBuilder listBlockBuilder;
    //private LabeledListBuilder labeledListBuilder;
    //private LiteralBlockBuilder literalBlockBuilder;
    //private ExampleBlockBuilder exampleBlockBuilder;
    //private SidebarBuilder sidebarBuilder;
    //private ListingBlockBuilder listingBlockBuilder;
    //private TableBuilder tableBuilder;


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

        listener.postProcess(); // TODO à remplacer par exitDocument

        return documentBuilder==null?null:documentBuilder.build();
    }

    // block title
    @Override
    public void blockTitleValue(char[] chars) {// TODO not yet tested
        state.setCurrentBlockTitle(chars);
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



    // text
    @Override
    public void formattedText(char[] chars) {
        state.pushText(new String(chars));
    }

    // document

    @Override
    public void enterDocument() {
        attributeEntryBuilder = new AttributeEntryBuilder();
        documentBuilder = DocumentBuilder.newBuilder(state);
    }

    @Override
    public void exitDocument() {
        //checkExitSection(-1); TODO OLIV
    }

    @Override
    public void documentTitle(String text) {
        documentBuilder.setTitle(text);
    }

    // author callbacks
    public void enterAuthors() {
    }

    @Override
    public void exitAuthors() {
    }

    @Override
    public void authorName(String name) {
        documentBuilder.getHeaderBuilder().setAuthorName(name);
    }

    @Override
    public void authorAddress(String email) {
        documentBuilder.getHeaderBuilder().setAuthorEmail(email);
    }

    @Override
    public void exitAuthor() {
        documentBuilder.getHeaderBuilder().closeAuthor();
    }

    // revision info callbacks

    @Override
    public void enterRevisionInfo() {
        documentBuilder.getHeaderBuilder().addRevisionInfo();
    }

    @Override @Deprecated // TODO à revoir pour décomposer en date, number, remark
    public void revisionInfo(String line) {
        documentBuilder.getHeaderBuilder().setRevisionInfoDate(line);
    }

    @Override
    public void exitRevisionInfo() {
    }


    @Override
    public void enterPreamble() {
        documentBuilder.getContentBuilder().addPreamble();
    }

    @Override
    public void exitPreamble() {
        documentBuilder.getContentBuilder().closePreamble();
    }

    // content callback

    @Override
    public void enterContent() {
    }

    @Override
    public void exitContent() {
        documentBuilder.getContentBuilder().closeContent();
    }

    // section callbacks
    @Override
    public void enterSection(NodeContext context) {
        int level = min(context.getIntAttribute("level", -1), 6);
        documentBuilder.getContentBuilder().newSection(level, attributeListBuilder.consume());
    }

    @Override
    public void sectionTitle(String title) {
        documentBuilder.getContentBuilder().setSectionTitle(title);
    }

    // paragraph
    @Override
    public void enterParagraph(String admonition) {
        admonition = admonition == null?null:admonition.toLowerCase();
        AttributeList attList = attributeListBuilder.consume();

        if (attList != null && "quote".equals(attList.getFirstPositionalAttribute())) {
            String attribution = attList.getSecondPositionalAttribute();
            String citationTitle = attList.getThirdPositionalAttribute();
            quoteBuilder = QuoteBuilder.of(attribution, citationTitle, attList);
            state.pushTextBlock(quoteBuilder);
        } else {
            paragraphBuilder = ParagraphBuilder.of(admonition, attList);
            state.pushTextBlock(paragraphBuilder);
        }
    }

    @Override
    public void exitParagraph() {
        BlockBuilder block = paragraphBuilder;
        block = (block == null) ? quoteBuilder:block;

        state.pushBlockToContainer(block);

        quoteBuilder = null;
        paragraphBuilder = null;
    }

    // list block
    @Override
    public void enterList() {
        listBlockBuilder = ListBlockBuilder.root(state.consumeBlockTitle());
        state.pushBlock(listBlockBuilder);
    }

    @Override
    public void exitList() {
        state.pushBlockToContainer(listBlockBuilder);
        listBlockBuilder = null;
        state.popBlock();
    }

    @Override
    public void enterListItem(NodeContext context) {
        int times = context.getIntAttribute("times.count", -1);
        int dots = context.getIntAttribute("dots.count", -1);

        ListItemBuilder builder = listBlockBuilder.newListItem(times, dots, attributeListBuilder.consume());

        state.pushTextBlock(builder);
        state.pushContainer(builder);
    }


    @Override
    public void exitListItem() {
        //handler.endListItem(currentList.level);
        state.popTextBlock();
        state.popContainer();
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

    // labeled list
    @Override
    public void enterLabeledList() {
        LabeledListBuilder builder = LabeledListBuilder.newBuilder();
        state.pushBlock(builder);
        state.pushBlockToContainer(builder);
    }

    @Override
    public void exitLabeledList() {
        state.popBlock();
    }

    @Override
    public void enterLabeledListItem() {
        LabeledListBuilder builder = state.peekBlock();
        builder.newItem();
    }

    @Override
    public void exitLabeledListItem() {

    }

    @Override
    public void enterLabeledListItemTitle() {
        LabeledListBuilder builder = state.peekBlock();
        this.state.pushTextBlock(
                new TextBlockBuilder() {
                    @Override
                    public void setText(String text) {
                        builder.setItemTitle(text);
                    }

                    @Override
                    public Block build() {
                        return null;
                    }
                }
        );
    }

    @Override
    public void exitLabeledListItemTitle() {
        state.popTextBlock();
    }


    @Override
    public void enterLabeledListItemContent() {

    }

    @Override
    public void exitLabeledListItemContent() {

    }

    @Override
    public void enterLabeledListItemSimpleContent() {
        LabeledListBuilder builder = state.peekBlock();
        state.pushTextBlock(
                new TextBlockBuilder() {
                    @Override
                    public void setText(String text) {
                        builder.setItemContent(text);
                    }

                    @Override
                    public Block build() {
                        return null;
                    }
                }
        );
    }

    @Override
    public void exitLabeledListItemSimpleContent() {
        state.popTextBlock();
    }

    // literal block
    @Override
    public void enterLiteralBlock() {
        LiteralBlockBuilder builder = LiteralBlockBuilder.newBuilder();
        state.pushBlock(builder);
        state.pushTextBlock(builder);
    }

    @Override
    public void exitLiteralBlock() {
        LiteralBlockBuilder builder = state.popBlock();
        state.popTextBlock();
        state.pushBlockToContainer(builder);
    }

    // example block
    @Override
    public void enterExample() {
        ExampleBlockBuilder builder = ExampleBlockBuilder.newBuilder(attributeListBuilder.consume());
        state.pushBlock(builder);
        state.pushContainer(builder);
    }

    @Override
    public void exitExample() {
        ExampleBlockBuilder builder = state.popBlock();
        state.popContainer();
        state.pushBlockToContainer(builder);
    }

    // sidebar
    @Override
    public void enterSidebar() {
        SidebarBuilder builder = SidebarBuilder.newBuilder();
        state.pushBlock(builder);
        state.pushContainer(builder);
    }

    @Override
    public void exitSidebar() {
        SidebarBuilder builder = state.peekBlock();
        state.popContainer();
        state.pushBlockToContainer(builder);
    }

    // listing block
    @Override
    public void enterListingBlock() {
        ListingBlockBuilder builder = ListingBlockBuilder.newBuilder(attributeListBuilder.consume());
        state.pushBlock(builder);
        state.pushTextBlock(builder);
    }

    @Override
    public void exitListingBlock() {
        ListingBlockBuilder builder = state.popBlock();
        state.popTextBlock();
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
        state.pushTextBlock(builder.addCallout());
    }

    @Override
    public void exitCallout() {
        state.popTextBlock();
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
        TableBuilder builder = TableBuilder.newBuilder(attributeListBuilder.consume(), lineNumber);

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
        builder.setContent(text);
    }
}
