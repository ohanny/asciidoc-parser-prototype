package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class DescriptionListItem extends Block {
    private Text content;

    public static DescriptionListItem of(Title title, Text content) {
        DescriptionListItem item = new DescriptionListItem();
        item.title = title;
        item.content = content;

        return item;
    }

    public Text getContent() {
        return content;
    }
}
