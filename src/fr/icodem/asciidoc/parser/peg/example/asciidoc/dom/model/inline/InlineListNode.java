package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;

import java.util.List;

public class InlineListNode extends InlineNode {
    private List<InlineNode> nodes;

    public static InlineListNode of(List<InlineNode> nodes) {
        InlineListNode node = new InlineListNode();
        node.type = ElementType.InlineListNode;
        node.nodes = nodes;

        return node;
    }

    public List<InlineNode> getNodes() {
        return nodes;
    }
}
