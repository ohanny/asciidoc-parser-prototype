package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class Document {
    private Header header;
    private Content content;

    public static Document of(Header header, Content content) {
        Document doc = new Document();
        doc.header = header;
        doc.content = content;
        return doc;
    }

    public Header getHeader() {
        return header;
    }

    public Content getContent() {
        return content;
    }

}
