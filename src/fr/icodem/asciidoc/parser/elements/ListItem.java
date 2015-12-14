package fr.icodem.asciidoc.parser.elements;

import java.util.List;

public class ListItem extends Element {
    protected String text;
    protected AbstractList nestedList;
    protected List<Block> blocks;

    public ListItem(String id, String text, AbstractList nestedList, List<Block> blocks) {
        super(id);
        this.text = text;
        this.nestedList = nestedList;
        this.blocks = blocks;
    }

    public String getText() {
        return text;
    }

    public AbstractList getNestedList() {
        return nestedList;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public boolean hasNestedList() {
        return nestedList != null;
    }
}
