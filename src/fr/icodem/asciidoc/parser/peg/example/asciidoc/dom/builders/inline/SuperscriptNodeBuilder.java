package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.DecoratorNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.InlineNode;

public class SuperscriptNodeBuilder extends InlineNodeBuilder {

    public static SuperscriptNodeBuilder newBuilder(AttributeList attList) {
        SuperscriptNodeBuilder builder = new SuperscriptNodeBuilder();
        builder.attributes = attList;

        return builder;
    }

    @Override
    public InlineNode build() {
        return DecoratorNode.superscript(attributes, buildChild());
    }

}
