package fr.icodem.asciidoc.parser.elements;

import java.util.List;
import java.util.Map;

public class ElementFactory {
    public Document document(DocumentTitle title, List<Author> authors,
                             Map<String, AttributeEntry> nameToAttributeMap, boolean headerPresent) {
        return new Document(title, authors, nameToAttributeMap, headerPresent);
    }

    public DocumentTitle documentTitle(String text) {
        return new DocumentTitle(text);
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

    public AttributeEntry attributeEntry(String name, String value) {
        return new AttributeEntry(name, value);
    }

    public Author author(String id, String name, String address, int position) {
        return new Author(id, name, address, position);
    }
}
