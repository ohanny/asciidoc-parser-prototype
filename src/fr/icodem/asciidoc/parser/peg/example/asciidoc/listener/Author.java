package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class Author {

    private int position;
    private String name;
    private String address;
    private String addressLabel;

    public Author(int position, String name, String address, String addressLabel) {
        this.position = position;
        this.name = name;
        this.address = address;
        this.addressLabel = addressLabel;
    }

    public static Author of(int position, String name, String address, String addressLabel) {
        return new Author(position, name, address, addressLabel);
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressLabel() {
        return addressLabel;
    }
}
