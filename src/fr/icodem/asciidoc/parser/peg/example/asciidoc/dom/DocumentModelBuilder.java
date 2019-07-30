package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntry;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document;
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
    //protected List<Attribute> attList;

    // builders
    private AttributeEntryBuilder attributeEntryBuilder;
    private AttributeListBuilder attributeListBuilder;
    private DocumentBuilder documentBuilder;
    private AuthorsBuilder authorsBuilder;
    private RevisionInfoBuilder revisionInfoBuilder;
    private SectionBuilder firstSection;
    private SectionBuilder currentSection;
    private ParagraphBuilder paragraphBuilder;
    private QuoteBuilder quoteBuilder;
    private ListBlockBuilder listBlockBuilder;
    private LiteralBlockBuilder literalBlockBuilder;
    private ExampleBlockBuilder exampleBlockBuilder;
    private ListingBlockBuilder listingBlockBuilder;


    //private TextBlockBuilder currentTextBlockBuilder;
    private Deque<TextBlockBuilder> textBlockBuilders;
    private Deque<BlockContainer> blockContainers;

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
        builder.textBlockBuilders = new LinkedList<>();
        builder.blockContainers = new LinkedList<>();

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

    private AttributeEntry getAttributeEntry(String name) {
        return attributeEntries.getAttribute(name);
    }

    // attribute list
//    private AttributeList consumeAttList() {
//        if (this.attList.isEmpty()) return null;
//        AttributeList attList = AttributeList.of(Collections.unmodifiableList(this.attList));
//        clearAttList();
//        return attList;
//    }

//    private void clearAttList() {
//        attList.clear();
//    }

    @Override
    public void attributeName(String name) {
        attributeListBuilder.setAttributeName(name);
//        textObjects.pop()
//                .setValue(value);
    }

    @Override
    public void attributeValue(String value) {
        attributeListBuilder.setAttributeValue(value);
//        textObjects.pop()
//                .setValue(value);
    }

    @Override
    public void enterIdAttribute() {
//        Text text = Text.empty();
//        attList.add(Attribute.of("id", text));
//        textObjects.push(text);
        attributeListBuilder.addIdAttribute();
    }

    @Override
    public void enterRoleAttribute() {
        //Text text = Text.empty();
        //attList.add(Attribute.of("role", text));
        //textObjects.push(text);
        attributeListBuilder.addRoleAttribute();
    }

    @Override
    public void enterOptionAttribute() {
        //Text text = Text.empty();
        //attList.add(Attribute.of("options", text));
        //textObjects.push(text);
        attributeListBuilder.addOptionAttribute();
    }

    @Override
    public void enterPositionalAttribute() {
        //Text value = Text.empty();
        //attList.add(Attribute.of((String)null, value));
        //textObjects.push(value);
        attributeListBuilder.addPositionalAttribute();
    }

    @Override
    public void enterNamedAttribute() {
        attributeListBuilder.addNamedAttribute();
//        Text name = Text.empty();
//        Text value = Text.empty();
//        attList.add(Attribute.of(name, value));
//
//        textObjects.push(value);
//        textObjects.push(name);
    }



    // text
    @Override
    public void formattedText(char[] chars) {
        //if (currentTextBlockBuilder != null) currentTextBlockBuilder.setText(new String(chars));
        TextBlockBuilder builder = textBlockBuilders.peekLast();
        if (builder != null) {
            builder.setText(new String(chars));
        }
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
        checkExitSection(-1);
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


    // content callback
    @Override
    public void exitContent() {
        SectionListBuilder builder = new SectionListBuilder();
        documentBuilder.setSections(builder.build(firstSection));
    }

    // section callbacks
    @Override
    public void enterSection(NodeContext context) {
        int level = min(context.getIntAttribute("level", -1), 6);

        // new section
        if (firstSection != null) {
            // close parents and get new section parent
            SectionBuilder parent = checkExitSection(level);

            SectionBuilder previous = currentSection;
            currentSection = SectionBuilder.of(level, previous, parent);
            previous.setNext(currentSection);
        } else {
            firstSection = SectionBuilder.of(level);
            currentSection = firstSection;
        }

        //currentSection.setAttList(consumeAttList());
        currentSection.setAttList(attributeListBuilder.consume());
        //handler.startSection(level, currentSection.attList);
        blockContainers.addLast(currentSection);
    }

    @Override
    public void sectionTitle(String title) {
        currentSection.setTitle(title);
        currentSection.setRef(textToRef(title));
        //int level = currentSection.getLevel();
        //String ref = currentSection.ref;
        //AttributeList attList = currentSection.attList;
        //handler.writeSectionTitle(level, title, ref, attList);
    }

    /**
     *
     * @param newSectionLevel new section level; -1 if not a new section, but the end of document
     * @return the parent level
     */
    private SectionBuilder checkExitSection(int newSectionLevel) {
        if (currentSection == null) return null;

        SectionBuilder parent = null;
        if (newSectionLevel == currentSection.getLevel()) {
            //handler.endSection(currentSection.getLevel());
            closeSection();
            parent = currentSection.getParent();
        } else if (newSectionLevel < currentSection.getLevel()) {
            //handler.endSection(currentSection.getLevel());
            closeSection();
            SectionBuilder p = currentSection.getParent();
            while (p != null) {
                if (p.getLevel() > newSectionLevel) {
                    closeSection();
                    //handler.endSection(p.getLevel());
                    p = p.getParent();
                } else if (p.getLevel() == newSectionLevel) {
                    closeSection();
                    //handler.endSection(p.getLevel());
                    parent = p.getParent();
                    break;
                } else {
                    parent = p;
                    break;
                }
            }
        } else {
            parent = currentSection;
        }

        return parent;
    }

    private void closeSection() {
        this.blockContainers.removeLast();
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
            //currentTextBlockBuilder = quoteBuilder;
            textBlockBuilders.addLast(quoteBuilder);
        } else {
            paragraphBuilder = ParagraphBuilder.of(admonition, attList);
            //currentTextBlockBuilder = paragraphBuilder;
            textBlockBuilders.addLast(paragraphBuilder);
        }
    }

    @Override
    public void exitParagraph() {
        BlockBuilder block = textBlockBuilders.removeLast();
        blockContainers.peekLast().addBlock(block);

        quoteBuilder = null;
        paragraphBuilder = null;
        //currentTextBlockBuilder = null;
    }

    // list block
    @Override
    public void enterList() {
        listBlockBuilder = ListBlockBuilder.root(consumeBlockTitle());
//        listBlockBuilder = ListBlockBuilder.newBuilder();
//        listBlockBuilder.setTitle(consumeBlockTitle());

//        currentList = ListContext.empty();
//        currentList.title = consumeBlockTitle();
//        handler.startList();
    }

    @Override
    public void exitList() {
        currentSection.addBlock(listBlockBuilder);
        listBlockBuilder = null;
//        while (currentList != null) {
//            if (currentList.type == ListType.Unordered) {
//                handler.endUnorderedList(currentList.level);
//            } else if (currentList.type == ListType.Ordered) {
//                handler.endOrderedList(currentList.level);
//            }
//            currentList = currentList.parent;
//        }
//        handler.endList();
    }

    @Override
    public void enterListItem(NodeContext context) {
        int times = context.getIntAttribute("times.count", -1);
        int dots = context.getIntAttribute("dots.count", -1);

        ListItemBuilder builder = listBlockBuilder.newListItem(times, dots, attributeListBuilder.consume());
        textBlockBuilders.addLast(builder);
        blockContainers.addLast(builder);

        /*
        int times, dots = 0;
        if ((times = context.getIntAttribute("times.count", -1)) > 0) {
            if (listBlockBuilder.isUnordered()) {
                if (times == listBlockBuilder.getBullets()) {

                } else if (times > listBlockBuilder.getBullets()) {
                    listBlockBuilder = ListBlockBuilder.withParent(listBlockBuilder);
                    listBlockBuilder.setBullets(times);
                } else if (times < listBlockBuilder.getBullets()) {
                    while (times < listBlockBuilder.getBullets() && listBlockBuilder.getLevel() > 1) {
                        //handler.endUnorderedList(currentList.level);
                        listBlockBuilder = listBlockBuilder.getParent();
                    }
                }
            } else if (listBlockBuilder.isOrdered()) {
                if (times > listBlockBuilder.getBullets()) {
                    listBlockBuilder = ListBlockBuilder.withParent(listBlockBuilder);
                    listBlockBuilder.setBullets(times);
                } else {
                    // find parent with same type and level
                    ListBlockBuilder ancestorWithSameLevel = listBlockBuilder.findParentListWithTypeAndLevel(ListBlockBuilder.Type.Unordered, times);
                    if (ancestorWithSameLevel == null) {
                        listBlockBuilder = ListBlockBuilder.withParent(listBlockBuilder);
                        listBlockBuilder.setBullets(times);
                    } else {
                        ListBlockBuilder parent = listBlockBuilder.getParent();
                        while (listBlockBuilder != parent) {
                            //handler.endUnorderedList(currentList.level);
                            listBlockBuilder = listBlockBuilder.getParent();
                        }
                    }
                }
            }
        } else if ((dots = context.getIntAttribute("dots.count", -1)) > 0) {
            if (listBlockBuilder.isOrdered()) {
                if (dots == currentList.bullets) {

                } else if (dots > currentList.bullets) {
                    currentList = ListContext.withParent(currentList);
                    currentList.bullets = dots;
                } else if (dots < currentList.bullets) {
                    while (dots < currentList.bullets && currentList.level > 1) {
                        handler.endOrderedList(currentList.level);
                        currentList = currentList.parent;
                    }
                }
            } else if (listBlockBuilder.isUnordered()) {
                // find parent with same type and level
                ListContext ancestorWithSameLevel = findParentListWithTypeAndLevel(currentList, ListType.Ordered, dots);
                if (ancestorWithSameLevel == null) {
                    currentList = ListContext.withParent(currentList);
                    currentList.bullets = dots;
                } else {
                    ListContext parent = currentList.parent;
                    while (currentList != parent) {
                        handler.endUnorderedList(currentList.level);
                        currentList = currentList.parent;
                    }
                }
            }
        }


        if (currentList.type == null) {
            if (times > 0) {
                currentList.type = ListType.Unordered;
                currentList.bullets = times;
                currentList.attList = consumeAttList();
                handler.startUnorderedList(currentList.level, currentList.attList, currentList.title);
            } else if (dots > 0) {
                currentList.type = ListType.Ordered;
                currentList.bullets = dots;
                currentList.attList = consumeAttList();
                handler.startOrderedList(currentList.level, currentList.attList);
            }
        }
        handler.startListItem(currentList.level, ++currentList.itemCount, currentList.attList);
        clearAttList();

         */
    }


    @Override
    public void exitListItem() {
        //handler.endListItem(currentList.level);
        textBlockBuilders.removeLast();
        blockContainers.removeLast();
    }

    @Override
    public void enterListItemValue() {
        //handler.startListItemValue();
    }

    @Override
    public void exitListItemValue() {
        //handler.endListItemValue();
    }

    // literal block
    @Override
    public void enterLiteralBlock() {
        literalBlockBuilder = LiteralBlockBuilder.newBuilder();
        textBlockBuilders.addLast(literalBlockBuilder);
    }

    @Override
    public void exitLiteralBlock() {
        BlockBuilder block = textBlockBuilders.removeLast();
        blockContainers.peekLast().addBlock(block);

        literalBlockBuilder = null;
    }

    // example block
    @Override
    public void enterExample() {
        exampleBlockBuilder = ExampleBlockBuilder.newBuilder();
        blockContainers.addLast(exampleBlockBuilder);
    }

    @Override
    public void exitExample() {
        blockContainers.removeLast();

        BlockContainer container = blockContainers.peekLast();
        if (container != null) {
            container.addBlock(exampleBlockBuilder);
        }

        exampleBlockBuilder = null;
    }

    // listing block
    @Override
    public void enterListingBlock() {
        listingBlockBuilder = ListingBlockBuilder.newBuilder(attributeListBuilder.consume());
        textBlockBuilders.addLast(listingBlockBuilder);
    }

    @Override
    public void exitListingBlock() {
        BlockBuilder block = textBlockBuilders.removeLast();
        blockContainers.peekLast().addBlock(block);

        listingBlockBuilder = null;
    }

    // callouts
    @Override
    public void enterCallouts() {
        listingBlockBuilder.newCallouts();
    }

    @Override
    public void enterCallout() {
        textBlockBuilders.addLast(listingBlockBuilder.addCallout());
    }

    @Override
    public void exitCallout() {
        textBlockBuilders.removeLast();
    }

    @Override
    public void calloutNumber(String nb) {
        listingBlockBuilder.setCalloutNumber(nb);
    }

}
