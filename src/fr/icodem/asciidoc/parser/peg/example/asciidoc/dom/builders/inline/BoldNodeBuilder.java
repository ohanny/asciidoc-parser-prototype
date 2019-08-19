package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.DecoratorNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.InlineNode;

public class BoldNodeBuilder extends InlineNodeBuilder {

    public static BoldNodeBuilder newBuilder(AttributeList attList) {
        BoldNodeBuilder builder = new BoldNodeBuilder();
        builder.attributes = attList;

        return builder;
    }

    @Override
    public InlineNode build() {
        return DecoratorNode.bold(attributes, buildChild());
    }

}
