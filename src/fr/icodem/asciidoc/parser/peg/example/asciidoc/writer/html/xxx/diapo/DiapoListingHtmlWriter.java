package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ListingBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.ListingHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.DIV;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.PRE;

public class DiapoListingHtmlWriter extends ListingHtmlWriter {

    public DiapoListingHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startListing(ListingBlock listing) {
        String classes = getMoreClasses("listingblock", listing.getAttributes());
        String style = styleBuilder().reset(listing.getAttributes()).addPosition().style();

        indent()
                .append(DIV.start("class", classes, "style", style))
                .nl()
                .incIndent()
                .writeBlockTitle(listing)
                .indent()
                .append(PRE.start("class", getMoreClasses(getListingPreClass(listing), listing.getAttributes())))
//                .forEach(listing.getLines(), (line, index) ->
//                        append(CODE.start("class", getListingCodeClass(listing.getLanguage(), line)))
//                                .forEach(line.getLineChunks(), this::writeListingLineChunk)
//                                .writeListingCallout(line)
//                                .append(CODE.end())
//                                .appendIf(listing.getLines().size() - 1 != index, () -> nl())
//                )
        ;

    }

    protected String getListingPreClass(ListingBlock listing) {
        boolean highlightjs = isAttributeValueEqualTo("source-highlighter", "highlightjs");
        boolean highlightSelective = isAttributeEnabled("highlight-selective");

        String preClass = null;
        if (!highlightSelective || listing.isHighlight()) {
            if (highlightjs && listing.isSource()) {
                preClass = "highlightjs highlight";
            } else if (listing.isSource()) {
                preClass = "highlight";
            }
        }

        if (preClass == null) preClass = "listingblock";
        else preClass += " listingblock";

        if (!listing.isLinenums()) {
            if (preClass == null) preClass = "nolinenums";
            else preClass += " nolinenums";
        }
        return preClass;
    }


    @Override
    protected void endListing(ListingBlock listing) {
        append(PRE.end()).nl()
          .decIndent()
            .indent().append(DIV.end()).nl()
        ;
    }

}
