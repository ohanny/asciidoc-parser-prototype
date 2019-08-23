package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.DescriptionListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DescriptionListItemBuilder implements BlockBuilder, TextContainer, BlockContainer {

    private String title;
    private String text;
    private InlineNode inline;
    private List<BlockBuilder> blocks;

    public static DescriptionListItemBuilder newBuilder() {
        return new DescriptionListItemBuilder();
    }

    @Override
    public DescriptionListItem build() {
        List<Block> blocks = null;
        if (this.blocks != null) {
            blocks = this.blocks
                    .stream()
                    .map(BlockBuilder::build)
                    .collect(Collectors.toList());
        }

        return DescriptionListItem.of(Title.of(title), Text.of(text, inline), blocks);
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
    public void setInline(InlineNode inline) {
        this.inline = inline;
    }

    @Override
    public void addBlock(BlockBuilder builder) {
        if (this.blocks == null) this.blocks = new ArrayList<>();
        this.blocks.add(builder);
    }
}
