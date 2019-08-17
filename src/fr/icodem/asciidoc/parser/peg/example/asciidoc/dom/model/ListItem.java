package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class ListItem extends TextBlock {
    private int position;
    protected List<Block> blocks;

    public static ListItem of(int position, Text text, List<Block> blocks) {
        ListItem item = new ListItem();
        item.type = ElementType.ListItem;
        item.position = position;
        item.text = text;
        item.blocks = blocks;

        return item;
    }

    public int getPosition() {
        return position;
    }

    public boolean hasBlocks() {
        return blocks != null && blocks.size() > 0;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
