package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class Header {
    private AttributeEntries attributes;
    private Title documentTitle;
    private List<Author> authors;
    private RevisionInfo revisionInfo;

    public static Header of(AttributeEntries attributes, Title documentTitle,
                            List<Author> authors, RevisionInfo revisionInfo) {
        Header header = new Header();
        header.attributes = attributes;
        header.documentTitle = documentTitle;
        header.authors = authors;
        header.revisionInfo = revisionInfo;

        return header;
    }

    public AttributeEntries getAttributes() {
        return attributes;
    }

    public Title getDocumentTitle() {
        return documentTitle;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public RevisionInfo getRevisionInfo() {
        return revisionInfo;
    }
}
