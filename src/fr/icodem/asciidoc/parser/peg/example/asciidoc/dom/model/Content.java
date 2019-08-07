package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class Content extends Block {
    private List<Section> sections;

    public static Content of(List<Section> sections) {
        Content content = new Content();
        content.sections = sections;

        return content;
    }

    public List<Section> getSections() {
        return sections;
    }
}
