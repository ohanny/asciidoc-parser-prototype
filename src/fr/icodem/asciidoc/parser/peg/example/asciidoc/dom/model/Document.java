package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class Document {
    private AttributeEntries attributes;
    private Header header;
    private Content content;

    public static Document of(AttributeEntries attributes, Header header, Content content) {
        Document doc = new Document();
        doc.attributes = attributes;
        doc.header = header;
        doc.content = content;
        return doc;
    }

    public boolean hasTitle() {
        return header != null && header.getDocumentTitle() != null;
    }

    public AttributeEntries getAttributes() {
        return attributes;
    }

    public Header getHeader() {
        return header;
    }

    public Content getContent() {
        return content;
    }

}
