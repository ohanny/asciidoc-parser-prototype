package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Callout;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ListingBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.Text;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ListingBlockBuilder implements BlockBuilder, TextContainer {

    private AttributeList attributeList;
    private String title;
    private String text;
    private Deque<CalloutBuilder> callouts;

    public static ListingBlockBuilder newBuilder(AttributeList attList, String title) {
        ListingBlockBuilder builder = new ListingBlockBuilder();
        builder.attributeList = attList;
        builder.title = title;
        return builder;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public ListingBlock build() {
        List<Callout> callouts = this.callouts == null?null:this.callouts
                .stream()
                .map(CalloutBuilder::build)
                .collect(Collectors.toList());

        return ListingBlock.of(attributeList, Title.of(title), Text.of(text), callouts);
    }

    public void newCallouts() {
        callouts = new LinkedList<>();
    }

    public CalloutBuilder addCallout() {
        CalloutBuilder builder = CalloutBuilder.newBuilder();
        callouts.addLast(builder);

        return builder;
    }

    public void setCalloutNumber(String number) {
        callouts.peekLast().setNumber(Integer.parseInt(number));
    }
}
