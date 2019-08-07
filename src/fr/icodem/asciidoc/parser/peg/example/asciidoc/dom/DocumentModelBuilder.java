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

    private AttributeEntries attributeEntries;

    // builders
    private AttributeEntryBuilder attributeEntryBuilder;
    private AttributeListBuilder attributeListBuilder;
    private DocumentBuilder documentBuilder;
    private AuthorsBuilder authorsBuilder;
    private RevisionInfoBuilder revisionInfoBuilder;
    private PreambleBuilder preambleBuilder;

    private ContentBuilder contentBuilder;
    private SectionBuilder firstSection;
    private SectionBuilder currentSection;


    private ParagraphBuilder paragraphBuilder;
    private QuoteBuilder quoteBuilder;
    private ListBlockBuilder listBlockBuilder;
    private LabeledListBuilder labeledListBuilder;
    private LiteralBlockBuilder literalBlockBuilder;
    private ExampleBlockBuilder exampleBlockBuilder;
    private SidebarBuilder sidebarBuilder;
    private ListingBlockBuilder listingBlockBuilder;
    private TableBuilder tableBuilder;

    private BuildState state;

    // TODO duplicate
    // computed refs : helps avoid duplicates
    private Map<String, Integer> refs = new HashMap<>();

    private char[] currentBlockTitle;


    public static DocumentModelBuilder newDocumentBuilder(AttributeEntries attributeEntries) {
        DocumentModelBuilder builder = new DocumentModelBuilder();
        builder.attributeEntries = attributeEntries;
        builder.rules = new BlockRules2(attributeEntries);
        builder.rules.withFactory(defaultRulesFactory());
        //builder.attList = new LinkedList<>();
        builder.attributeListBuilder = AttributeListBuilder.newBuilder();

        builder.state = BuildState.newInstance();

        return builder;
    }

    public Document build(String text) {
        final BlockListener2 listener = new BlockListener2(this, attributeEntries);

        ParsingResult result = new ParseRunner(rules, rules::document)
                //.trace()
                .parse(new StringReader(text), listener, null, null);

        listener.postProcess(); // TODO à remplacer par exitDocument

        return documentBuilder==null?null:documentBuilder.build();
    }

    private String textToRef(String text) {
        String ref = text.toLowerCase().replaceAll("\\s+", "_");
        int count = refs.getOrDefault(ref, 0);
        refs.put(ref, ++count);
        if (count > 1) {
            ref = ref + "_" + count;
        }
        return ref;
    }

    // block title
    @Override
    public void blockTitleValue(char[] chars) {// TODO not yet tested
        currentBlockTitle = chars;
    }

    private String consumeBlockTitle() {
        String title = currentBlockTitle == null?null:new String(currentBlockTitle);
        currentBlockTitle = null;

        return title;
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
        attributeEntries.addAttribute(att);
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
        documentBuilder = new DocumentBuilder();
        documentBuilder.setAttributes(attributeEntries);
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
        authorsBuilder = new AuthorsBuilder();
        authorsBuilder.init();
    }

    @Override
    public void exitAuthors() {
        documentBuilder.setAuthors(authorsBuilder.build());
    }

    @Override
    public void authorName(String name) {
        authorsBuilder.setName(name);
    }

    @Override
    public void authorAddress(String email) {
        authorsBuilder.setEmail(email);
    }

    @Override
    public void exitAuthor() {
        authorsBuilder.buildAuthor();
    }

    // revision info callbacks

    @Override
    public void enterRevisionInfo() {
        revisionInfoBuilder = new RevisionInfoBuilder();
    }

    @Override @Deprecated // TODO à revoir pour décomposer en date, number, remark
    public void revisionInfo(String line) {
        revisionInfoBuilder.setDate(line);
    }

    @Override
    public void exitRevisionInfo() {
        documentBuilder.setRevisionInfo(revisionInfoBuilder.build());
    }


    @Override
    public void enterPreamble() {
        preambleBuilder = PreambleBuilder.newBuilder();
        state.pushContainer(preambleBuilder);
    }

    @Override
    public void exitPreamble() {
        documentBuilder.setPreamble(preambleBuilder.build());
        state.popContainer();
        preambleBuilder = null;

    }

    // content callback


    @Override
    public void enterContent() {
        contentBuilder = ContentBuilder.newBuilder(this::closeSection);
    }

    @Override
    public void exitContent() {
        documentBuilder.setSections(contentBuilder.build().getSections());
        contentBuilder.exitDocument();
    }

    // section callbacks
    @Override
    public void enterSection(NodeContext context) {
        int level = min(context.getIntAttribute("level", -1), 6);

        SectionBuilder currentSection = contentBuilder.newSection(level, attributeListBuilder.consume());


        state.pushContainer(currentSection); // TODO move to section builder
    }

    @Override
    public void sectionTitle(String title) {
        contentBuilder.setSectionTitle(title);
    }

    private void closeSection() {
        state.popContainer();
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
        listBlockBuilder = ListBlockBuilder.root(consumeBlockTitle());
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
        labeledListBuilder = LabeledListBuilder.newBuilder();
        state.pushBlockToContainer(labeledListBuilder);
    }

    @Override
    public void exitLabeledList() {
        labeledListBuilder = null;
    }

    @Override
    public void enterLabeledListItem() {
        labeledListBuilder.newItem();
    }

    @Override
    public void exitLabeledListItem() {

    }

    @Override
    public void enterLabeledListItemTitle() {
        this.state.pushTextBlock(
                new TextBlockBuilder() {
                    @Override
                    public void setText(String text) {
                        labeledListBuilder.setItemTitle(text);
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
        state.pushTextBlock(
                new TextBlockBuilder() {
                    @Override
                    public void setText(String text) {
                        labeledListBuilder.setItemContent(text);
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
        literalBlockBuilder = LiteralBlockBuilder.newBuilder();
        state.pushTextBlock(literalBlockBuilder);
    }

    @Override
    public void exitLiteralBlock() {
        BlockBuilder block = state.popTextBlock();
        state.pushBlockToContainer(block);

        literalBlockBuilder = null;
    }

    // example block
    @Override
    public void enterExample() {
        exampleBlockBuilder = ExampleBlockBuilder.newBuilder(attributeListBuilder.consume());
        state.pushContainer(exampleBlockBuilder);
    }

    @Override
    public void exitExample() {
        state.popContainer();
        state.pushBlockToContainer(exampleBlockBuilder);
        exampleBlockBuilder = null;
    }

    // sidebar
    @Override
    public void enterSidebar() {
        sidebarBuilder = SidebarBuilder.newBuilder();
        state.pushContainer(sidebarBuilder);
    }

    @Override
    public void exitSidebar() {
        state.popContainer();
        state.pushBlockToContainer(sidebarBuilder);
        sidebarBuilder = null;
    }

    // listing block
    @Override
    public void enterListingBlock() {
        listingBlockBuilder = ListingBlockBuilder.newBuilder(attributeListBuilder.consume());
        state.pushTextBlock(listingBlockBuilder);
    }

    @Override
    public void exitListingBlock() {
        state.popTextBlock();
        state.pushBlockToContainer(listingBlockBuilder);

        listingBlockBuilder = null;
    }

    // callouts
    @Override
    public void enterCallouts() {
        listingBlockBuilder.newCallouts();
    }

    @Override
    public void enterCallout() {
        state.pushTextBlock(listingBlockBuilder.addCallout());
    }

    @Override
    public void exitCallout() {
        state.popTextBlock();
    }

    @Override
    public void calloutNumber(String nb) {
        listingBlockBuilder.setCalloutNumber(nb);
    }

    // horizontal rule
    @Override
    public void horizontalRule() {
        state.pushBlockToContainer(HorizontalRuleBuilder.newBuilder());
    }

    // table
    @Override
    public void enterTable(int lineNumber) {
        tableBuilder = TableBuilder.newBuilder(attributeListBuilder.consume(), lineNumber);

        state.pushBlockToContainer(tableBuilder);
    }

    @Override
    public void exitTable() {
        tableBuilder.tableEnd();
        tableBuilder = null;
    }

    @Override
    public void enterTableCell(int lineNumber) {
        tableBuilder.addCell(lineNumber);
    }

    @Override
    public void exitTableCell() {

    }

    @Override
    public void tableBlock(String text) {
        tableBuilder.setContent(text);
    }
}
