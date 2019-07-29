package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ExampleBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExampleBlockBuilder implements BlockBuilder, BlockContainer {
    private List<BlockBuilder> blocks;

    public static ExampleBlockBuilder newBuilder() {
        ExampleBlockBuilder builder = new ExampleBlockBuilder();
        builder.blocks = new ArrayList<>();

        return builder;
    }

    @Override
    public ExampleBlock build() {
        List<Block> blocks = this.blocks
                .stream()
                .map(BlockBuilder::build)
                .collect(Collectors.toList());

        return ExampleBlock.of(blocks);
    }

    @Override
    public void addBlock(BlockBuilder builder) {
        this.blocks.add(builder);
    }
}
