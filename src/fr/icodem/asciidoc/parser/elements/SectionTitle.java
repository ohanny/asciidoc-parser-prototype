package fr.icodem.asciidoc.parser.elements;

public class SectionTitle extends Element {
    private int level;

    public SectionTitle(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
