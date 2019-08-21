package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineListNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;

import java.util.List;
import java.util.stream.Collectors;

public class InlineBuilder {

    private InlineBuildState state;

    public static InlineBuilder newBuilder(InlineBuildState state) {
        InlineBuilder builder = new InlineBuilder();
        builder.state = state;

        return builder;
    }

    public InlineNode build() {
        List<InlineNode> nodes = state.getBuilders()
                .stream()
                .map(InlineNodeBuilder::build)
                .collect(Collectors.toList());

        if (nodes.size() == 1) {
            return nodes.get(0);
        }

        return InlineListNode.of(nodes);
    }

}
