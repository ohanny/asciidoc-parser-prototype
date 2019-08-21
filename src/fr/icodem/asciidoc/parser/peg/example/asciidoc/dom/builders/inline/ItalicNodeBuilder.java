package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.DecoratorNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;

public class ItalicNodeBuilder extends InlineNodeBuilder {

    public static ItalicNodeBuilder newBuilder(AttributeList attList) {
        ItalicNodeBuilder builder = new ItalicNodeBuilder();
        builder.attributes = attList;

        return builder;
    }

    @Override
    public InlineNode build() {
        return DecoratorNode.italic(attributes, buildChild());
    }

}
