package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;

import java.util.List;

public class DocumentBuilder {
    private BuildState state;

    //private AttributeEntries attributes;
    //private String title;
    //private List<Author> authors;
    //private RevisionInfo revisionInfo;

    private HeaderBuilder headerBuilder;
    private ContentBuilder contentBuilder;

    public static DocumentBuilder newBuilder(BuildState state) {
        DocumentBuilder builder = new DocumentBuilder();
        builder.state = state;
        builder.headerBuilder = HeaderBuilder.newBuilder(state);
        builder.contentBuilder = ContentBuilder.newBuilder(state);

        return builder;
    }

    public Document build() {

        //Header header = Header.of(attributes, Title.of(title), authors, revisionInfo);
        Header header = headerBuilder.build();
        Content content = contentBuilder.build();

        Document doc = Document.of(header, content);

        return doc;
    }

    public void setTitle(String title) {
        //this.title = title;
        headerBuilder.setTitle(title);
    }

//    public void setAttributes(AttributeEntries attributes) {
//        this.attributes = attributes;
//        //headerBuilder.setAttributes(attributes);
//    }

//    public void setAuthors(List<Author> authors) {
//        this.authors = authors;
//    }
//
//    public void setRevisionInfo(RevisionInfo revisionInfo) {
//        this.revisionInfo = revisionInfo;
//    }

//    public HeaderBuilder addHeader() {
//        headerBuilder = HeaderBuilder.newBuilder();
//        return headerBuilder;
//    }

    // TODO change
    public ContentBuilder getContentBuilder() {
        return contentBuilder;
    }

    public HeaderBuilder getHeaderBuilder() {
        return headerBuilder;
    }
}
