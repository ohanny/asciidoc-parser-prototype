package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.UrlUtils;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.AttributeListBuilder;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2.InlineHandler2;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2.InlineListener2;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.rules2.InlineRules2;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;

public class InlineModelBuilder implements InlineHandler2 {
    private InlineRules2 rules;
    private InlineBuildState state;

    // builders
    private AttributeListBuilder attributeListBuilder;

    public static InlineModelBuilder newBuilder(AttributeEntries attributeEntries) {
        InlineModelBuilder builder = new InlineModelBuilder();
        builder.rules = new InlineRules2(attributeEntries);
        builder.rules.withFactory(defaultRulesFactory());
        builder.state = InlineBuildState.newInstance(attributeEntries);
        builder.attributeListBuilder = AttributeListBuilder.newBuilder();

        return builder;
    }

    public InlineNode build(String text) {
        InlineListener2 listener = new InlineListener2(this);
        ParsingResult result = new ParseRunner(rules, rules::formattedText)
                //.trace()
                .parse(new StringReader(text), listener, null, null);

        return InlineBuilder.newBuilder(state).build();
    }

    // attributes
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

    // xref
    @Override
    public void enterXRef() {
        state.pushNode(XRefNodeBuilder.newBuilder());
    }

    @Override
    public void xrefValue(String value) {
        XRefNodeBuilder builder = state.peekNode();
        builder.setValue(value);
        builder.setInternal(UrlUtils.isUrl(value));
    }

    @Override
    public void xrefLabel(String label) {
        XRefNodeBuilder builder = state.peekNode();
        builder.setLabel(label);
    }

    @Override
    public void exitXRef() {
        state.popNode();
    }


    // raw text
    @Override
    public void writeText(String text) {
        state.addNode(TextNodeBuilder.newBuilder(text));
    }

    // markup
    @Override
    public void enterBold() {
        state.pushNode(BoldNodeBuilder.newBuilder(attributeListBuilder.consume()));
    }

    @Override
    public void exitBold() {
        state.popNode();
    }

    @Override
    public void enterItalic() {
        state.pushNode(ItalicNodeBuilder.newBuilder(attributeListBuilder.consume()));
    }

    @Override
    public void exitItalic() {
        state.popNode();
    }

    @Override
    public void enterSubscript() {
        state.pushNode(SubscriptNodeBuilder.newBuilder(attributeListBuilder.consume()));
    }

    @Override
    public void exitSubscript() {
        state.popNode();
    }

    @Override
    public void enterSuperscript() {
        state.pushNode(SuperscriptNodeBuilder.newBuilder(attributeListBuilder.consume()));
    }

    @Override
    public void exitSuperscript() {
        state.popNode();
    }

    @Override
    public void enterMonospace() {
        state.pushNode(MonospaceNodeBuilder.newBuilder(attributeListBuilder.consume()));
    }

    @Override
    public void exitMonospace() {
        state.popNode();
    }
}
