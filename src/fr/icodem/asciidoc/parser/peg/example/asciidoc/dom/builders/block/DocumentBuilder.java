package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Content;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Header;

public class DocumentBuilder {
    private BlockBuildState state;
    private HeaderBuilder headerBuilder;
    private ContentBuilder contentBuilder;

    public static DocumentBuilder newBuilder(BlockBuildState state) {
        DocumentBuilder builder = new DocumentBuilder();
        builder.state = state;

        return builder;
    }

    public Document build() {
        Header header = headerBuilder == null ? null : headerBuilder.build();
        Content content = contentBuilder == null ? null : contentBuilder.build();

        return Document.of(state.getAttributeEntries(), header, content);
    }

    public void setTitle(String title) {
        headerBuilder.setTitle(title);
    }

    public ContentBuilder addContent() {
        return contentBuilder = ContentBuilder.newBuilder(state);
    }

    public HeaderBuilder addHeader() {
        return headerBuilder = HeaderBuilder.newBuilder();
    }

}
