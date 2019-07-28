package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Text;

public class ListItemBuilder implements TextBlockBuilder {
    private String text;

    public static ListItemBuilder newBuilder() {
        ListItemBuilder builder = new ListItemBuilder();
        return builder;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public ListItem build() {
        return ListItem.of(Text.of(text));
    }
}
