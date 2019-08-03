package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Preamble;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PreambleBuilder implements BlockContainer {

    private List<BlockBuilder> blocks;

    public static PreambleBuilder newBuilder() {
        PreambleBuilder builder = new PreambleBuilder();
        builder.blocks = new ArrayList<>();

        return builder;
    }

    public Preamble build() {
        List<Block> blocks = this.blocks
                .stream()
                .map(BlockBuilder::build)
                .collect(Collectors.toList());

        return Preamble.of(blocks);
    }

    @Override
    public void addBlock(BlockBuilder builder) {
        this.blocks.add(builder);
    }
}
