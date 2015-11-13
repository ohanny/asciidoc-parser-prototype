package fr.icodem.asciidoc.parser.elements;

public class SectionTitle extends Element {
    private int level;
    private String text;

    public SectionTitle(int level, String text) {
        super(null);
        this.level = level;
        this.text = text;
    }

    public int getLevel() {
        return level;
    }

    public String getText() {
        return text;
    }
}
