package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class DocumentHeader {
    private AttributeEntries attributes;
    private Title documentTitle;
    private List<Author> authors;
    private RevisionInfo revisionInfo;

    public static DocumentHeader of(AttributeEntries attributes, Title documentTitle,
                                    List<Author> authors, RevisionInfo revisionInfo) {
        DocumentHeader header = new DocumentHeader();
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
