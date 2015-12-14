package fr.icodem.asciidoc.parser.elements;

import java.util.List;

public class OrderedList extends AbstractList {
    public OrderedList(AttributeList attList, List<ListItem> items, int level) {
        super(attList, items, level);
    }
}
