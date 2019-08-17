package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ExampleBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExampleBlockBuilder implements BlockBuilder, BlockContainer {
    private AttributeList attList;
    private String title;
    private List<BlockBuilder> blocks;

    public static ExampleBlockBuilder newBuilder(AttributeList attList, String title) {
        ExampleBlockBuilder builder = new ExampleBlockBuilder();
        builder.attList = attList;
        builder.title = title;
        builder.blocks = new ArrayList<>();

        return builder;
    }

    @Override
    public ExampleBlock build() {
        List<Block> blocks = this.blocks
                .stream()
                .map(BlockBuilder::build)
                .collect(Collectors.toList());

        return ExampleBlock.of(attList, Title.of(title), blocks, getAdmonition());
    }

    private String getAdmonition() {
        String admonition = null;
        if (attList != null) {
            if (attList.hasPositionalAttributes("NOTE")) {
                admonition = "note";
            } else if (attList.hasPositionalAttributes("TIP")) {
                admonition = "tip";
            } else if (attList.hasPositionalAttributes("IMPORTANT")) {
                admonition = "important";
            } else if (attList.hasPositionalAttributes("WARNING")) {
                admonition = "warning";
            } else if (attList.hasPositionalAttributes("CAUTION")) {
                admonition = "caution";
            } else if (attList.hasPositionalAttributes("WARNING")) {
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
