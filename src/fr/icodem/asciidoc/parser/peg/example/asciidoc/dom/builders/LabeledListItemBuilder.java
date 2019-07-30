package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.LabeledListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Text;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Title;

public class LabeledListItemBuilder implements BlockBuilder {

    private String title;
    private String content;

    public static LabeledListItemBuilder newBuilder() {
        return new LabeledListItemBuilder();
    }

    @Override
    public LabeledListItem build() {
        return LabeledListItem.of(Title.of(title), Text.of(content));
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
