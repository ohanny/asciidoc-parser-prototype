package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.InlineModelBuilder;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.InlineNode;

@FunctionalInterface
public interface TextContainer {
    void setText(String text);

    default String getText() {
        return null;
    }

    default void parseText(AttributeEntries attributeEntries) {
        if (getText() == null) return;

        InlineModelBuilder builder = InlineModelBuilder.newBuilder(attributeEntries);
        setInline(builder.build(getText()));
    }

    default void setInline(InlineNode inline) {}
}
