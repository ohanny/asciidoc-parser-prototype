package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class Document {
    private DocumentHeader header;
    private List<Section> sections;

    public static Document of(DocumentHeader header, List<Section> sections) {
        Document doc = new Document();
        doc.header = header;
        doc.sections = sections;
        return doc;
    }

    public DocumentHeader getHeader() {
        return header;
    }

    public List<Section> getSections() {
        return sections;
    }
}
