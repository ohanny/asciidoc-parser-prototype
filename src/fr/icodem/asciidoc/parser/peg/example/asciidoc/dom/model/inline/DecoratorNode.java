package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;

public class DecoratorNode extends InlineNode {
    private AttributeList attributes;
    private InlineNode node;

    private static DecoratorNode newInstance(ElementType type, AttributeList attributes, InlineNode node) {
        DecoratorNode decorator = new DecoratorNode();
        decorator.type = type;
        decorator.attributes = attributes;
        decorator.node = node;

        return decorator;
    }

    public static DecoratorNode bold(AttributeList attributes, InlineNode node) {
        return newInstance(ElementType.BoldNode, attributes, node);
    }

    public static DecoratorNode italic(AttributeList attributes, InlineNode node) {
        return newInstance(ElementType.ItalicNode, attributes, node);
    }

    public static DecoratorNode superscript(AttributeList attributes, InlineNode node) {
        return newInstance(ElementType.SuperscriptNode, attributes, node);
    }

    public static DecoratorNode subscript(AttributeList attributes, InlineNode node) {
        return newInstance(ElementType.SubscriptNode, attributes, node);
    }

    public static DecoratorNode monospace(AttributeList attributes, InlineNode node) {
        return newInstance(ElementType.MonospaceNode, attributes, node);
    }

    public static DecoratorNode mark(AttributeList attributes, InlineNode node) {
        return newInstance(ElementType.MarkNode, attributes, node);
    }

    public AttributeList getAttributes() {
        return attributes;
    }

    public <T extends InlineNode> T getNode() {
        return (T)node;
    }

}
