package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.DecoratorNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.InlineNode;

public class MarkNodeBuilder extends InlineNodeBuilder {

    public static MarkNodeBuilder newBuilder(AttributeList attList) {
        MarkNodeBuilder builder = new MarkNodeBuilder();
        builder.attributes = attList;

        return builder;
    }

    @Override
    public InlineNode build() {
        return DecoratorNode.mark(attributes, buildChild());
    }

}
