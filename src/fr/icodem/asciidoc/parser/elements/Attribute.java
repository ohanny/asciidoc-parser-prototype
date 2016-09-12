package fr.icodem.asciidoc.parser.elements;

public class Attribute {
    protected Text name;
    private Text text;

    public Attribute(String name, String value) {
        this.name = Text.withValue(name);
        this.text = Text.withValue(value);
    }

    public Attribute(String name, Text text) {
        this.name = Text.withValue(name);
        this.text = text;
    }

    public Attribute(Text name, Text text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name.getValue();
    }

    public Object getValue() {
        return text.getValue();
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", value='" + text + '\'' +
                '}';
    }
}
