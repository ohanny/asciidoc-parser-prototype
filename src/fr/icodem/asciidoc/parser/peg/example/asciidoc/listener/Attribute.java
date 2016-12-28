package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class Attribute {
    private Text name;
    private Text value;

    public static Attribute of(String name, String value) {
        return new Attribute(name, value);
    }

    public static Attribute of(String name, Text text) {
        return new Attribute(name, text);
    }

    public static Attribute of(Text name, Text value) {
        return new Attribute(name, value);
    }

    private Attribute(String name, String value) {
        this.name = Text.of(name);
        this.value = Text.of(value);
    }

    private Attribute(String name, Text value) {
        this.name = Text.of(name);
        this.value = value;
    }

    private Attribute(Text name, Text value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name.getValue();
    }

    public Object getValue() {
        return value.getValue();
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name=" + name +
                ", value=" + value +
                '}';
    }
}
