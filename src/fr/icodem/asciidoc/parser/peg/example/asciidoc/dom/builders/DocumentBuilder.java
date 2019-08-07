package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;

import java.util.List;

public class DocumentBuilder {
    private AttributeEntries attributes;
    private String title;
    private List<Author> authors;
    private RevisionInfo revisionInfo;

    private Preamble preamble;

    private ContentBuilder contentBuilder;

    public Document build() {

        DocumentHeader header = DocumentHeader.of(attributes, Title.of(title), authors, revisionInfo);

        Content content = null;
        if (contentBuilder != null) {
            content = contentBuilder.build();
        }

        Document doc = Document.of(header, content, preamble);

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

    public void setPreamble(Preamble preamble) {
        this.preamble = preamble;
    }

    public void setContentBuilder(ContentBuilder contentBuilder) {
        this.contentBuilder = contentBuilder;
    }

}
