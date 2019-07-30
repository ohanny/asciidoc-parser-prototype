package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class LabeledList extends Block {
    private List<LabeledListItem> items;

    public static LabeledList of(List<LabeledListItem> items) {
        LabeledList list = new LabeledList();
        list.type = ElementType.LabeledList;
        list.items = items;

        return list;
    }

    public List<LabeledListItem> getItems() {
        return items;
    }
}
