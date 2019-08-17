package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Section;

import java.util.ArrayList;
import java.util.List;

public class SectionListBuilder {

    public static SectionListBuilder newBuilder() {
        return new SectionListBuilder();
    }

    public List<Section> build(SectionBuilder first) {
        List<Section> sections = new ArrayList<>();

        SectionBuilder current = first;
        while (current != null) {
            sections.add(current.build());
            current = current.getNext();
        }

        return sections;
    }

}
