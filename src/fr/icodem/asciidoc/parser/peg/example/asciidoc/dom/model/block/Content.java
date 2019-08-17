package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import java.util.List;

public class Content extends Block {
    private Preamble preamble;
    private List<Section> sections;

    public static Content of(Preamble preamble, List<Section> sections) {
        Content content = new Content();
        content.sections = sections;
        content.preamble = preamble;

        return content;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Preamble getPreamble() {
        return preamble;
    }
}
