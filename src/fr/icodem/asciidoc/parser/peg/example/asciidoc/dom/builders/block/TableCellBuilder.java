package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableCell;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableCellBuilder implements BlockBuilder, TextContainer, BlockContainer {
    private String text;
    private List<BlockBuilder> blocks;

    public static TableCellBuilder newBuilder() {
        TableCellBuilder cell = new TableCellBuilder();
        return cell;
    }

    @Override
    public TableCell build() {
        List<Block> blocks = null;
        if (this.blocks != null) {
            blocks = this.blocks
                    .stream()
                    .map(BlockBuilder::build)
                    .collect(Collectors.toList());
        }

        return TableCell.of(Text.of(text), blocks, 0, 0);
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
    public void addBlock(BlockBuilder builder) {
        if (this.blocks == null) this.blocks = new ArrayList<>();
        this.blocks.add(builder);
    }

}
