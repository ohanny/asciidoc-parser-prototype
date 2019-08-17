package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Author;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Header;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.RevisionInfo;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;

import java.util.List;

public class HeaderBuilder {
    private String title;

    private AuthorsBuilder authorsBuilder;
    private RevisionInfoBuilder revisionInfoBuilder;

    public static HeaderBuilder newBuilder() {
        HeaderBuilder builder = new HeaderBuilder();
        builder.authorsBuilder = AuthorsBuilder.newBuilder();

        return builder;
    }

    public Header build() {
        List<Author> authors = authorsBuilder.build();
        RevisionInfo revisionInfo = revisionInfoBuilder == null ? null : revisionInfoBuilder.build();

        return Header.of(Title.of(title), authors, revisionInfo);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthorName(String name) {
        authorsBuilder.setName(name);
    }

    public void setAuthorEmail(String email) {
        authorsBuilder.setEmail(email);
    }

    public void closeAuthor() {
        authorsBuilder.buildAuthor();
    }

    public void addRevisionInfo() {
        revisionInfoBuilder = RevisionInfoBuilder.newBuilder();
    }

    public void setRevisionInfoDate(String date) {
        revisionInfoBuilder.setDate(date);
    }

}
