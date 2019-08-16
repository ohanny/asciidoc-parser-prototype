package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class Sidebar extends Block {
    protected List<Block> blocks;

    public static Sidebar of(AttributeList attList, Title title, List<Block> blocks) {
        Sidebar sidebar = new Sidebar();
        sidebar.type = ElementType.Sidebar;
        sidebar.attributes = attList;
        sidebar.title = title;
        sidebar.blocks = blocks;

        return sidebar;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

}
