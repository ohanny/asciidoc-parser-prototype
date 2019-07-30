package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Sidebar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SidebarBuilder implements BlockBuilder, BlockContainer {
    private List<BlockBuilder> blocks;

    public static SidebarBuilder newBuilder() {
        SidebarBuilder builder = new SidebarBuilder();
        builder.blocks = new ArrayList<>();

        return builder;
    }

    @Override
    public Sidebar build() {
        List<Block> blocks = this.blocks
                .stream()
                .map(BlockBuilder::build)
                .collect(Collectors.toList());

        return Sidebar.of(blocks);
    }

    @Override
    public void addBlock(BlockBuilder builder) {
        this.blocks.add(builder);
    }
}
