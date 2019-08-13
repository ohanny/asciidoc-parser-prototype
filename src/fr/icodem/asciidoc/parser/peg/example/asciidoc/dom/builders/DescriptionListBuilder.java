package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.DescriptionList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.DescriptionListItem;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DescriptionListBuilder implements BlockBuilder {

    private Deque<DescriptionListItemBuilder> items;

    public static DescriptionListBuilder newBuilder() {
        DescriptionListBuilder builder = new DescriptionListBuilder();
        builder.items = new LinkedList<>();

        return builder;
    }

    @Override
    public DescriptionList build() {
        List<DescriptionListItem> items = this.items
                .stream()
                .map(DescriptionListItemBuilder::build)
                .collect(Collectors.toList());

        return DescriptionList.of(items);
    }

    public void newItem() {
        this.items.add(DescriptionListItemBuilder.newBuilder());
    }

    public void setItemTitle(String title) {
        this.items.peekLast().setTitle(title);
    }

    public void setItemContent(String content) {
        this.items.peekLast().setContent(content);
    }

}
