package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class ListItem extends TextBlock {

    public static ListItem of(Text text) {
        ListItem item = new ListItem();
        item.type = ElementType.ListItem;
        item.text = text;

        return item;
    }

}
