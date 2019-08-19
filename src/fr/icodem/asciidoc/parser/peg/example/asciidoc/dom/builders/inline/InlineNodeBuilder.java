package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.InlineListNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.InlineNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class InlineNodeBuilder {
    protected AttributeList attributes;
    protected List<InlineNodeBuilder> children;

    public abstract InlineNode build();

    public void addChild(InlineNodeBuilder child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    protected InlineNode buildChild() {
        List<InlineNode> nodes = children
                .stream()
                .map(InlineNodeBuilder::build)
                .collect(Collectors.toList());

        if (nodes.size() == 1) {
            return nodes.get(0);
        }

        return InlineListNode.of(nodes);
    }
}
