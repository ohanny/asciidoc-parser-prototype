package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import java.util.List;

public class Header {
    private Title documentTitle;
    private List<Author> authors;
    private RevisionInfo revisionInfo;

    public static Header of(Title documentTitle, List<Author> authors, RevisionInfo revisionInfo) {
        Header header = new Header();
        header.documentTitle = documentTitle;
        header.authors = authors;
        header.revisionInfo = revisionInfo;

        return header;
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
