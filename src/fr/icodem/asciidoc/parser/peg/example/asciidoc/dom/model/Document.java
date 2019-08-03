package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class Document {
    private DocumentHeader header;
    private Preamble preamble;
    private List<Section> sections;

    public static Document of(DocumentHeader header, Preamble preamble, List<Section> sections) {
        Document doc = new Document();
        doc.header = header;
        doc.preamble = preamble;
        doc.sections = sections;
        return doc;
    }

    public DocumentHeader getHeader() {
        return header;
    }

    public Preamble getPreamble() {
        return preamble;
    }

    public List<Section> getSections() {
        return sections;
    }

}
