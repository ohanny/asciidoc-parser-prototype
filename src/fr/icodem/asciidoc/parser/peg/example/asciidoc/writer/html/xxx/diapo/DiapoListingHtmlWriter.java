package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ListingBlock;
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
        indent()
                .append(DIV.start("class", getMoreClasses("listingblock", listing.getAttributes()), "style", styleBuilder().reset(listing.getAttributes()).addPosition().style()))
                .nl()
                .incIndent()
                .writeBlockTitle(listing)
//                .appendIf(listing.getTitle() != null, () ->
//                        writeBlockTitle(listing.getTitle())
//                )
                .indent()
                .append(PRE.start("class", getMoreClasses(getListingPreClass(listing), listing.getAttributes())))
//                .forEach(listing.getLines(), (line, index) ->
//                        append(CODE.start("class", getListingCodeClass(listing.getLanguage(), line)))
//                                .forEach(line.getLineChunks(), this::writeListingLineChunk)
//                                .writeListingCallout(line)
//                                .append(CODE.end())
//                                .appendIf(listing.getLines().size() - 1 != index, () -> nl())
//                )
                .append(PRE.end())
                .nl()
        ;

    }

    protected String getListingPreClass(ListingBlock listing) {
        /*
        String preClass = super.getListingPreClass(listing);
        if (preClass == null) preClass = "listingblock";
        else preClass += " listingblock";

        if (!listing.isLinenums()) {
            if (preClass == null) preClass = "nolinenums";
            else preClass += " nolinenums";
        }
        return preClass;

         */
        return null;
    }


    @Override
    protected void endListing(ListingBlock listing) {
        decIndent()
          .indent().append(DIV.end()).nl()
        ;
    }

}
