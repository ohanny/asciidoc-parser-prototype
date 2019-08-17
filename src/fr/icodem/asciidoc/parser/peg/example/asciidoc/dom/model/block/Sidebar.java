package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;

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
