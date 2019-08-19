package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.DecoratorNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.InlineNode;

public class MonospaceNodeBuilder extends InlineNodeBuilder {

    public static MonospaceNodeBuilder newBuilder(AttributeList attList) {
        MonospaceNodeBuilder builder = new MonospaceNodeBuilder();
        builder.attributes = attList;

        return builder;
    }

    @Override
    public InlineNode build() {
        return DecoratorNode.monospace(attributes, buildChild());
    }

}
