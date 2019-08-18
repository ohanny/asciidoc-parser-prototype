package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.Text;

import java.util.List;

public class TableCell extends TextBlock {
    private int colspan;
    private int rowspan;
    protected List<Block> blocks;

    public static TableCell of(Text text, List<Block> blocks, int colspan, int rowspan) {
        TableCell cell = new TableCell();
        cell.text = text;
        cell.blocks = blocks;
        cell.colspan = colspan;
        cell.rowspan = rowspan;

        return cell;
    }

    public int getColspan() {
        return colspan;
    }

    public int getRowspan() {
        return rowspan;
    }

    public boolean hasBlocks() {
        return blocks != null && blocks.size() > 0;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

}
