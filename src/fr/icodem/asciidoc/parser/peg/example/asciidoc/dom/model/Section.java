package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class Section extends Block {
    protected int level;
    protected Title title;
    protected List<Block> blocks;

    public static Section of(AttributeList attList, int level, Title title, List<Block> blocks) {
        Section section = new Section();
        section.type = ElementType.Section;
        section.attributes = attList;
        section.level = level;
        section.title = title;
        section.blocks = blocks;

        return section;
    }

    public int getLevel() {
        return level;
    }

    public Title getTitle() {
        return title;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

}
