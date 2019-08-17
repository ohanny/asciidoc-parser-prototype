package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

public class Author {
    private String fullName;
    private String lastName;
    private String firstName;
    private String middleName;
    private String email;
    private String initials;

    public static Author of(String fullName, String lastName, String firstName,
            String middleName, String email, String initials) {
        Author author = new Author();
        author.fullName = fullName;
        author.lastName = lastName;
        author.firstName = firstName;
        author.middleName = middleName;
        author.email = email;
        author.initials = initials;

        return author;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getEmail() {
        return email;
    }

    public String getInitials() {
        return initials;
    }
}
