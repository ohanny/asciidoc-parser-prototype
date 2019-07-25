package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Section;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Title;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SectionListBuilder {
    public List<Section> build(SectionBuilder first) {
        List<Section> sections = new ArrayList<>();

        SectionBuilder current = first;
        while (current != null) {
            //Section section = Section.of(current.getLevel(), Title.of(current.getTitle()), null);
            sections.add(current.build());
            current = current.getNext();
        }

        return sections;
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
