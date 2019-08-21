package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Callout;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TextBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

import java.util.List;

public class ListingBlock extends TextBlock {

    private boolean source;
    private String language;
    private boolean linenums;
    private boolean highlight;

    private List<ListingLine> lines;
    private List<Callout> callouts;

    public static ListingBlock of(AttributeList attList, Title title, Text text, List<Callout> callouts, List<ListingLine> lines) {
        ListingBlock listing = new ListingBlock();
        listing.type = ElementType.Listing;
        listing.attributes = attList;
        listing.title = title;
        listing.text = text;
        listing.callouts = callouts;
        listing.lines = lines;

        if (attList != null && "source".equals(attList.getFirstPositionalAttribute())) {
            listing.source = true;
            listing.language = attList.getSecondPositionalAttribute();
            listing.language = ((listing.language == null)) ? null : listing.language.toLowerCase();
            listing.linenums = attList.hasPositionalAttributes("linenums");
            listing.highlight = attList.hasOption("highlight");
        }

        return listing;
    }

    public boolean isSource() {
        return source;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isLinenums() {
        return linenums;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public List<Callout> getCallouts() {
        return callouts;
    }

    public List<ListingLine> getLines() {
        return lines;
    }
}
