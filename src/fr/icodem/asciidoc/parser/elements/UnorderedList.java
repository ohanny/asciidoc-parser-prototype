package fr.icodem.asciidoc.parser.elements;

import java.util.List;

public class UnorderedList extends AbstractList {
    public UnorderedList(AttributeList attList, List<ListItem> items, int level) {
        super(attList, items, level);
    }
}
