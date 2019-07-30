package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.LabeledList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.LabeledListItem;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class LabeledListBuilder implements BlockBuilder {

    private Deque<LabeledListItemBuilder> items;

    public static LabeledListBuilder newBuilder() {
        LabeledListBuilder builder = new LabeledListBuilder();
        builder.items = new LinkedList<>();

        return builder;
    }

    @Override
    public LabeledList build() {
        List<LabeledListItem> items = this.items
                .stream()
                .map(LabeledListItemBuilder::build)
                .collect(Collectors.toList());

        return LabeledList.of(items);
    }

    public void newItem() {
        this.items.add(LabeledListItemBuilder.newBuilder());
    }

    public void setItemTitle(String title) {
        this.items.peekLast().setTitle(title);
    }

    public void setItemContent(String content) {
        this.items.peekLast().setContent(content);
    }

}
