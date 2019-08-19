package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.DecoratorNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.InlineNode;

public class SubscriptNodeBuilder extends InlineNodeBuilder {

    public static SubscriptNodeBuilder newBuilder(AttributeList attList) {
        SubscriptNodeBuilder builder = new SubscriptNodeBuilder();
        builder.attributes = attList;

        return builder;
    }

    @Override
    public InlineNode build() {
        return DecoratorNode.subscript(attributes, buildChild());
    }

}
