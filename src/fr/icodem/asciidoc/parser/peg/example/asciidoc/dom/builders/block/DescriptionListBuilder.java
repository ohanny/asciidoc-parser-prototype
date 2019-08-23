package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.DescriptionList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.DescriptionListItem;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DescriptionListBuilder implements BlockBuilder {

    private BlockBuildState state;
    private AttributeList attributeList;
    private TitleBuilder title;
    private Deque<DescriptionListItemBuilder> items;

    public static DescriptionListBuilder newBuilder(BlockBuildState state) {
        DescriptionListBuilder builder = new DescriptionListBuilder();
        builder.state = state;
        builder.attributeList = state.consumeAttributeList();
        builder.title = state.consumeBlockTitle();
        builder.items = new LinkedList<>();

        return builder;
    }

    @Override
    public DescriptionList build() {
        List<DescriptionListItem> items = this.items
                .stream()
                .map(DescriptionListItemBuilder::build)
                .collect(Collectors.toList());

        return DescriptionList.of(attributeList, buildTitle(title), items);
    }

    public DescriptionListItemBuilder newItem() {
        this.items.add(DescriptionListItemBuilder.newBuilder());
        return items.peekLast();
    }

    public void setItemTitle(String title) {
        TitleBuilder builder = TitleBuilder.newBuilder(title);
        state.pushTextToParse(builder);

        this.items.peekLast().setTitle(builder);
    }

    public void setItemContent(String content) {
        this.items.peekLast().setText(content);
    }

}
