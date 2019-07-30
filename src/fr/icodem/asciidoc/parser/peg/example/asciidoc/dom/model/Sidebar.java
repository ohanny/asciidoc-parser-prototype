package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class Sidebar extends Block {
    protected List<Block> blocks;

    public static Sidebar of(List<Block> blocks) {
        Sidebar block = new Sidebar();
        block.type = ElementType.Sidebar;
        block.blocks = blocks;

        return block;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

}
