package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;

import java.util.List;

public class DocumentBuilder {
    private AttributeEntries attributes;
    private String title;
    private List<Author> authors;
    private RevisionInfo revisionInfo;

    private List<Section> sections;

    public Document build() {

        DocumentHeader header = DocumentHeader.of(attributes, Title.of(title), authors, revisionInfo);

        Document doc = Document.of(header, sections);

        return doc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAttributes(AttributeEntries attributes) {
        this.attributes = attributes;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void setRevisionInfo(RevisionInfo revisionInfo) {
        this.revisionInfo = revisionInfo;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
