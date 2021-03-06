package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListItemBuilder implements BlockBuilder, TextContainer, BlockContainer {
    private int position;
    private String text;
    private InlineNode inline;

    private List<BlockBuilder> blocks;

    public static ListItemBuilder newBuilder(int position) {
        ListItemBuilder builder = new ListItemBuilder();
        builder.position = position;
        return builder;
    }

    @Override
    public void setText(String text) {
        if (text != null) {
            this.text = text.trim();
        } else {
            this.text = null;
        }
    }

    @Override
    public String getText() {
        return text;
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

        return ListItem.of(position, Text.of(text, inline), blocks);
    }

    @Override
    public void addBlock(BlockBuilder builder) {
        if (this.blocks == null) this.blocks = new ArrayList<>();
        this.blocks.add(builder);
    }

    @Override
    public void setInline(InlineNode inline) {
        this.inline = inline;
    }
}
