package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

// TODO SectionTree relevant ?
public class SectionBranch extends Section {
    private List<SectionBranch> sections;

    public static SectionBranch of(int level, Title title, List<Block> blocks, List<SectionBranch> sections) {
        SectionBranch section = new SectionBranch();
        section.level = level;
        section.title = title;
        section.blocks = blocks;
        section.sections = sections;

        return section;
    }

    public List<SectionBranch> getSections() {
        return sections;
    }

}
