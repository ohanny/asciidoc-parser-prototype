package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;

import java.util.List;

public class DescriptionList extends Block {
    private List<DescriptionListItem> items;

    public static DescriptionList of(AttributeList attributes, Title title, List<DescriptionListItem> items) {
        DescriptionList list = new DescriptionList();
        list.type = ElementType.DescriptionList;
        list.attributes = attributes;
        list.title = title;
        list.items = items;

        return list;
    }

    public List<DescriptionListItem> getItems() {
        return items;
    }
}
