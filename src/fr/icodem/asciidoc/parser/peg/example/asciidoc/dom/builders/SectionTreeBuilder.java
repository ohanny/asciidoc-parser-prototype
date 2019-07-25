package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Section;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.SectionTree;

import java.util.Collections;
import java.util.List;

// TODO SectionTree relevant ?
public class SectionTreeBuilder {
    public SectionTree build(SectionBuilder first) {
        return null;
    }

    private boolean isLeaf(SectionBuilder builder) {
        return builder.getNext() == null || builder.getNext().getLevel() <= builder.getLevel();
    }

    private boolean isLastChild(SectionBuilder builder) {
        return builder.getNext() == null || builder.getNext().getLevel() < builder.getLevel();
    }

    private SectionBuilder getNextSibling(SectionBuilder builder) {
        return null;
    }

    private List<Section> buildChildren(SectionBuilder builder) {
        List<Section> children = null;
        if (hasChildren(builder)) {
            children = buildChildren(builder.getNext());
        } else {
            children = Collections.emptyList();
        }

        return null;
    }

    private boolean hasChildren(SectionBuilder builder) {
        return builder.getNext() != null && builder.getNext().getLevel() > builder.getLevel();
    }

    private List<SectionBuilder> getChildren(SectionListBuilder builder) {
        return null;
    }

}
