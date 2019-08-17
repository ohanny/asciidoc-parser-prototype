package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;

import java.util.List;

public class ListBlock extends Block {
    private int level;
    private List<ListItem> items;

    public static ListBlock of(AttributeList attList, Title title, ElementType type, int level, List<ListItem> items) {
        ListBlock list = new ListBlock();
        list.type = type;
        list.attributes = attList;
        list.title = title;
        list.level = level;
        list.items = items;

        return list;
    }

    public int getLevel() {
        return level;
    }

    public List<ListItem> getItems() {
        return items;
    }

}
