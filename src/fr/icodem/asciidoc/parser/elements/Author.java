package fr.icodem.asciidoc.parser.elements;

import fr.icodem.asciidoc.parser.Text;

public class Author extends Element {
    private Text name;
    private Text address;
    private int position;

    public Author(String id, String name, String address, int position) {
        super(id);
        this.name = Text.withValue(name);
        this.address = Text.withValue(address);
        this.position = position;
    }

    public Author(String id, Text name, Text address, int position) {
        super(id);
        this.name = name;
        this.address = address;
        this.position = position;
    }

    public String getName() {
        return (name == null)?null:name.getValue();
    }

    public String getAddress() {
        return (address == null)?null:name.getValue();
    }

    public int getPosition() {
        return position;
    }
}
