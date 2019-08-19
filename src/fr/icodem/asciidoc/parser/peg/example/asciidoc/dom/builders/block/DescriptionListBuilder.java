package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.DescriptionList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.DescriptionListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DescriptionListBuilder implements BlockBuilder {

    private AttributeList attributeList;
    private String title;
    private Deque<DescriptionListItemBuilder> items;

    public static DescriptionListBuilder newBuilder(AttributeList attList, String title) {
        DescriptionListBuilder builder = new DescriptionListBuilder();
        builder.attributeList = attList;
        builder.title = title;
        builder.items = new LinkedList<>();

        return builder;
    }

    @Override
    public DescriptionList build() {
        List<DescriptionListItem> items = this.items
                .stream()
                .map(DescriptionListItemBuilder::build)
                .collect(Collectors.toList());

        return DescriptionList.of(attributeList, Title.of(title), items);
    }

    public void newItem() {
        this.items.add(DescriptionListItemBuilder.newBuilder());
    }

    public void setItemTitle(String title) {
        this.items.peekLast().setTitle(title);
    }

    public void setItemContent(String content) {
        this.items.peekLast().setText(content);
    }

}
