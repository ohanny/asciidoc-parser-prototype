package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class LabeledListItem extends Block {
    private Text content;

    public static LabeledListItem of(Title title, Text content) {
        LabeledListItem item = new LabeledListItem();
        item.title = title;
        item.content = content;

        return item;
    }

    public Text getContent() {
        return content;
    }
}
