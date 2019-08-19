package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Author;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthorsBuilder {

    private List<Author> authors;

    // collected data for one author
    private String name;
    private String email;

    public static AuthorsBuilder newBuilder() {
        AuthorsBuilder builder = new AuthorsBuilder();
        builder.authors = new ArrayList<>();

        return builder;
    }

    private void resetAuthorData() {
        name = null;
        email = null;
    }

    public void buildAuthor() {
        String[] names = name.split("\\s");
        String firstName = names[0];
        String lastName = names.length > 1 ? names[1]:null;
        String middleName = null;
        String initials = lastName == null ? null : (firstName.substring(0, 1) + lastName.substring(0, 1)).toLowerCase();

        authors.add(Author.of(name, lastName, firstName, middleName, email, initials));
        resetAuthorData();
    }

    public List<Author> build() {
        List<Author> authors = Collections.unmodifiableList(this.authors);
        this.authors = null;
        return authors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
