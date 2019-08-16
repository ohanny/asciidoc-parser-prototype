package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class ListingBlock extends TextBlock {

    private List<Callout> callouts;

    public static ListingBlock of(AttributeList attList, Title title, Text text, List<Callout> callouts) {
        ListingBlock block = new ListingBlock();
        block.type = ElementType.Listing;
        block.attributes = attList;
        block.title = title;
        block.text = text;
        block.callouts = callouts;

        return block;
    }

    public List<Callout> getCallouts() {
        return callouts;
    }
}
