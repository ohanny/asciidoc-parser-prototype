package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class Sidebar extends Block {
    protected List<Block> blocks;

    public static Sidebar of(AttributeList attList, List<Block> blocks) {
        Sidebar sidebar = new Sidebar();
        sidebar.type = ElementType.Sidebar;
        sidebar.attributes = attList;
        sidebar.blocks = blocks;

        return sidebar;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

}
