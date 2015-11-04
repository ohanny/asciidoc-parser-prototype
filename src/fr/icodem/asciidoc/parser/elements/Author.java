package fr.icodem.asciidoc.parser.elements;

public class Author extends Element {
    private String name;
    private String address;
    private int position;

    public Author(String id, String name, String address, int position) {
        super(id);
        this.name = name;
        this.address = address;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getPosition() {
        return position;
    }
}
