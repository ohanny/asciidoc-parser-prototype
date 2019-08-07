package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class Document {
    private DocumentHeader header;
    private Content content;

    public static Document of(DocumentHeader header, Content content) {
        Document doc = new Document();
        doc.header = header;
        doc.content = content;
        return doc;
    }

    public DocumentHeader getHeader() {
        return header;
    }

    public Content getContent() {
        return content;
    }

}
