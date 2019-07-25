package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

// TODO SectionTree relevant ?
public class SectionTree {
    private List<SectionBranch> sections;

    public static SectionTree of(List<SectionBranch> sections) {
        SectionTree tree = new SectionTree();
        tree.sections = sections;

        return tree;
    }

    public List<SectionBranch> getSections() {
        return sections;
    }

}
