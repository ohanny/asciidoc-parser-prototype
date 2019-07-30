package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ExampleBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExampleBlockBuilder implements BlockBuilder, BlockContainer {
    private AttributeList attributeList;
    private List<BlockBuilder> blocks;

    public static ExampleBlockBuilder newBuilder(AttributeList attList) {
        ExampleBlockBuilder builder = new ExampleBlockBuilder();
        builder.blocks = new ArrayList<>();
        builder.attributeList = attList;

        return builder;
    }

    @Override
    public ExampleBlock build() {
        List<Block> blocks = this.blocks
                .stream()
                .map(BlockBuilder::build)
                .collect(Collectors.toList());

        return ExampleBlock.of(blocks, getAdmonition());
    }

    private String getAdmonition() {
        String admonition = null;
        if (attributeList != null) {
            if (attributeList.hasPositionalAttributes("NOTE")) {
                admonition = "note";
            } else if (attributeList.hasPositionalAttributes("TIP")) {
                admonition = "tip";
            } else if (attributeList.hasPositionalAttributes("IMPORTANT")) {
                admonition = "important";
            } else if (attributeList.hasPositionalAttributes("WARNING")) {
                admonition = "warning";
            } else if (attributeList.hasPositionalAttributes("CAUTION")) {
                admonition = "caution";
            } else if (attributeList.hasPositionalAttributes("WARNING")) {
                admonition = "warning";
            }
        }

        return admonition;
    }

    @Override
    public void addBlock(BlockBuilder builder) {
        this.blocks.add(builder);
    }
}
