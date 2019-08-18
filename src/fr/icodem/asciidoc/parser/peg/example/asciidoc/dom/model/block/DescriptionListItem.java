package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.Text;

import java.util.List;

public class DescriptionListItem extends TextBlock {
    protected List<Block> blocks;

    public static DescriptionListItem of(Title title, Text text, List<Block> blocks) {
        DescriptionListItem item = new DescriptionListItem();
        item.type = ElementType.DescriptionListItem;
        item.title = title;
        item.text = text;
        item.blocks = blocks;


        return item;
    }

    public boolean hasBlocks() {
        return blocks != null && blocks.size() > 0;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

}
