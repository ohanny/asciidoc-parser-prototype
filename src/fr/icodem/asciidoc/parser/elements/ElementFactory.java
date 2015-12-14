package fr.icodem.asciidoc.parser.elements;

import java.util.List;
import java.util.Map;

public class ElementFactory {

    public DocumentHeader documentHeader(Title title, List<Author> authors,
                             Map<String, AttributeEntry> nameToAttributeMap, boolean headerPresent) {
        return new DocumentHeader(title, authors, nameToAttributeMap, headerPresent);
    }

    public Paragraph paragraph(AttributeList attList, String text) {
        return new Paragraph(attList, text);
    }

    public Section section() {
        return new Section();
    }

    public SectionTitle sectionTitle(int level, String text) {
        return new SectionTitle(level, text);
    }

    public Title title(String text) {
        return new Title(text);
    }

    public AttributeEntry attributeEntry(String name, String value, boolean disabled) {
        return new AttributeEntry(name, value, disabled);
    }

    public Attribute attribute(String name, String value) {
        return new Attribute(name, value);
    }

    public AttributeList attributeList(List<Attribute> attributes) {
        return new AttributeList(attributes);
    }

    public Author author(String id, String name, String address, int position) {
        return new Author(id, name, address, position);
    }

    public ListItem listItem(String id, String text, AbstractList nestedList, List<Block> blocks) {
        return new ListItem(id, text, nestedList, blocks);
    }

    public OrderedList orderedList(AttributeList attList, List<ListItem> items, int level) {
        return new OrderedList(attList, items, level);
    }
    public UnorderedList unorderedList(AttributeList attList, List<ListItem> items, int level) {
        return new UnorderedList(attList, items, level);
    }
}
