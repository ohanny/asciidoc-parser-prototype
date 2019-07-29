package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class ListItem extends TextBlock {
    protected List<Block> blocks;

    public static ListItem of(Text text, List<Block> blocks) {
        ListItem item = new ListItem();
        item.type = ElementType.ListItem;
        item.text = text;
        item.blocks = blocks;

        return item;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
