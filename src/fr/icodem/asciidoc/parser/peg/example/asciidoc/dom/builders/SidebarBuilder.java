package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Sidebar;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Title;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SidebarBuilder implements BlockBuilder, BlockContainer {
    private AttributeList attList;
    private String title;
    private List<BlockBuilder> blocks;

    public static SidebarBuilder newBuilder(AttributeList attList, String title) {
        SidebarBuilder builder = new SidebarBuilder();
        builder.attList = attList;
        builder.title = title;
        builder.blocks = new ArrayList<>();

        return builder;
    }

    @Override
    public Sidebar build() {
        List<Block> blocks = this.blocks
                .stream()
                .map(BlockBuilder::build)
                .collect(Collectors.toList());

        return Sidebar.of(attList, Title.of(title), blocks);
    }

    @Override
    public void addBlock(BlockBuilder builder) {
        this.blocks.add(builder);
    }
}
