package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListItemBuilder implements TextBlockBuilder, BlockContainer {
    private String text;

    private List<BlockBuilder> blocks;

    public static ListItemBuilder newBuilder() {
        ListItemBuilder builder = new ListItemBuilder();
        return builder;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public ListItem build() {
        List<Block> blocks = null;
        if (this.blocks != null) {
            blocks = this.blocks
                    .stream()
                    .map(BlockBuilder::build)
                    .collect(Collectors.toList());
        }

        return ListItem.of(Text.of(text), blocks);
    }

    @Override
    public void addBlock(BlockBuilder builder) {
        if (this.blocks == null) this.blocks = new ArrayList<>();
        this.blocks.add(builder);
    }
}