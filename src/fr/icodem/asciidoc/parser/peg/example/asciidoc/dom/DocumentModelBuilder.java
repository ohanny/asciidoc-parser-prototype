package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.BlockListenerDelegate;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2.AsciidocHandler2;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2.BlockListener2;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.rules2.BlockRules2;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.util.*;

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


    private TextBlockBuilder currentTextBlockBuilder;

    // computed refs : helps avoid duplicates
    private Map<String, Integer> refs = new HashMap<>();


    public static DocumentModelBuilder newDocumentBuilder(AttributeEntries attributeEntries) {
        DocumentModelBuilder builder = new DocumentModelBuilder();
        builder.attributeEntries = attributeEntries;
        builder.rules = new BlockRules2(attributeEntries);
        builder.rules.withFactory(defaultRulesFactory());
        //builder.attList = new LinkedList<>();
        builder.attributeListBuilder = AttributeListBuilder.newBuilder();

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
        if (currentTextBlockBuilder != null) currentTextBlockBuilder.setText(new String(chars));
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
            parent = currentSection.getParent();
        } else if (newSectionLevel < currentSection.getLevel()) {
            //handler.endSection(currentSection.getLevel());
            SectionBuilder p = currentSection.getParent();
            while (p != null) {
                if (p.getLevel() > newSectionLevel) {
                    //handler.endSection(p.getLevel());
                    p = p.getParent();
                } else if (p.getLevel() == newSectionLevel) {
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

    // paragraph
    @Override
    public void enterParagraph(String admonition) {
        admonition = admonition == null?null:admonition.toLowerCase();
        AttributeList attList = attributeListBuilder.consume();

        if (attList != null && "quote".equals(attList.getFirstPositionalAttribute())) {
            String attribution = attList.getSecondPositionalAttribute();
            String citationTitle = attList.getThirdPositionalAttribute();
            quoteBuilder = QuoteBuilder.of(attribution, citationTitle, attList);
            currentTextBlockBuilder = quoteBuilder;
        } else {
            paragraphBuilder = ParagraphBuilder.of(admonition, attList);
            currentTextBlockBuilder = paragraphBuilder;
        }
    }

    @Override
    public void exitParagraph() {
        currentSection.addBlock(currentTextBlockBuilder);

        quoteBuilder = null;
        paragraphBuilder = null;
        currentTextBlockBuilder = null;
    }
}
