package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class Document {
    private DocumentHeader header;
    private Preamble preamble;
    private Content content;

    public static Document of(DocumentHeader header, Content content, Preamble preamble) {
        Document doc = new Document();
        doc.header = header;
        doc.content = content;
        doc.preamble = preamble;
        return doc;
    }

    public DocumentHeader getHeader() {
        return header;
    }

    public Content getContent() {
        return content;
    }

    public Preamble getPreamble() {
        return preamble;
    }

}
