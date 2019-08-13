package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.DescriptionListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Text;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Title;

public class DescriptionListItemBuilder implements BlockBuilder {

    private String title;
    private String content;

    public static DescriptionListItemBuilder newBuilder() {
        return new DescriptionListItemBuilder();
    }

    @Override
    public DescriptionListItem build() {
        return DescriptionListItem.of(Title.of(title), Text.of(content));
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
