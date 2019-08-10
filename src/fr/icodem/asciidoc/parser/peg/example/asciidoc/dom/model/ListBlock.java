package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class ListBlock extends Block {

    private List<ListItem> items;

    public static ListBlock of(AttributeList attList, ElementType type, List<ListItem> items) {
        ListBlock list = new ListBlock();
        list.attributes = attList;
        list.type = type;
        list.items = items;

        return list;
    }

    public List<ListItem> getItems() {
        return items;
    }

}
