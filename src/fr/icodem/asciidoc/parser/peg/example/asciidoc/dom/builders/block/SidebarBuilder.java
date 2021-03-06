package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Sidebar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SidebarBuilder implements BlockBuilder, BlockContainer {
    private AttributeList attList;
    private TitleBuilder title;
    private List<BlockBuilder> blocks;

    public static SidebarBuilder newBuilder(BlockBuildState state) {
        SidebarBuilder builder = new SidebarBuilder();
        builder.attList = state.consumeAttributeList();
        builder.title = state.consumeBlockTitle();
        builder.blocks = new ArrayList<>();

        return builder;
    }

    @Override
    public Sidebar build() {
        List<Block> blocks = this.blocks
                .stream()
                .map(BlockBuilder::build)
                .collect(Collectors.toList());

        return Sidebar.of(attList, buildTitle(title), blocks);
    }

    @Override
    public void addBlock(BlockBuilder builder) {
        this.blocks.add(builder);
    }
}
