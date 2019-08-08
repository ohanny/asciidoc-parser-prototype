package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;

import java.util.List;

public class DocumentBuilder {
    private HeaderBuilder headerBuilder;
    private ContentBuilder contentBuilder;

    public static DocumentBuilder newBuilder(BuildState state) {
        DocumentBuilder builder = new DocumentBuilder();
        builder.headerBuilder = HeaderBuilder.newBuilder(state);
        builder.contentBuilder = ContentBuilder.newBuilder(state);

        return builder;
    }

    public Document build() {

        Header header = headerBuilder.build();
        Content content = contentBuilder.build();

        return Document.of(header, content);
    }

    public void setTitle(String title) {
        headerBuilder.setTitle(title);
    }

    // TODO change
    public ContentBuilder getContentBuilder() {
        return contentBuilder;
    }

    public HeaderBuilder getHeaderBuilder() {
        return headerBuilder;
    }


}
