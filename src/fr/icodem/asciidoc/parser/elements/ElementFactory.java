package fr.icodem.asciidoc.parser.elements;

public class ElementFactory {
    public Document document() {
        return new Document();
    }

    public DocumentTitle documentTitle() {
        return new DocumentTitle();
    }

    public Paragraph paragraph(String text) {
        return new Paragraph(text);
    }

    public Section section() {
        return new Section();
    }

    public SectionTitle sectionTitle(int level) {
        return new SectionTitle(level);
    }

    public Title title(String text) {
        return new Title(text);
    }
}
