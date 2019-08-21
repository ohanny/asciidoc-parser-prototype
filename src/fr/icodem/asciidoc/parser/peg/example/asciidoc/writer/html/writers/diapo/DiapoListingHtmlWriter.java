package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.ListingBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.ListingLine;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.ListingLineChunk;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.ListingHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.*;

public class DiapoListingHtmlWriter extends ListingHtmlWriter<DiapoListingHtmlWriter> {

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
                /*.forEach(listing.getLines(), (line, index) ->
                        append(CODE.start("class", getListingCodeClass(listing.getLanguage(), line)))
                                .forEach(line.getLineChunks(), this::writeListingLineChunk)
                                .writeListingCallout(line)
                                .append(CODE.end())
                                .appendIf(listing.getLines().size() - 1 != index, () -> nl())
                )*/
        ;

    }

    private void writeListingLineChunk(ListingLineChunk chunk) {


        appendIf(chunk.isMark() && chunk.getMarkLevel() == 0, () ->
                append(MARK.start())
                        .writeTextOrChunks(chunk)
                        .append(MARK.end())
        )
                .appendIf(chunk.isMark() && chunk.getMarkLevel() > 0, () ->
                        append(MARK.start("class", "mark" + chunk.getMarkLevel()))
                                .writeTextOrChunks(chunk)
                                .append(MARK.end())
                )
                .appendIf(chunk.isStrong() && chunk.getStrongLevel() == 0, () ->
                        append(STRONG.start())
                                .writeTextOrChunks(chunk)
                                .append(STRONG.end())
                )
                .appendIf(chunk.isStrong() && chunk.getStrongLevel() > 0, () ->
                        append(STRONG.start("class", "strong" + chunk.getStrongLevel()))
                                .writeTextOrChunks(chunk)
                                .append(STRONG.end())
                )
                .appendIf(chunk.isImportant(), () ->
                        append(MARK.start("class", "important"))
                                .writeTextOrChunks(chunk)
                                .append(MARK.end())
                )
                .appendIf(chunk.isComment(), () ->
                        append(SPAN.start("class", "comment"))
                                .writeTextOrChunks(chunk)
                                .append(SPAN.end())
                )
                .appendIf(chunk.isNotMarked(), () ->
                        writeTextOrChunks(chunk)
                );
    }

    private DiapoListingHtmlWriter writeTextOrChunks(ListingLineChunk chunk) {
        if (chunk.getChunks() == null) {
            append(chunk.getText());
        } else {
            chunk.getChunks()
                    .stream()
                    .forEach(this::writeListingLineChunk);
        }
        return this;
    }

    protected DiapoListingHtmlWriter writeListingCallout(ListingLine line) {
        if (line.getCallouts() == null) return this;
        return
                forEach(line.getCallouts(), c ->
                        append(I.start("class", "conum", "data-value", Integer.toString(c.getNumber())))
                                .append(I.end())
                                .append(B.start())
                                .append("(")
                                .append(Integer.toString(c.getNumber()))
                                .append(")")
                                .append(B.end())
                                .append(" ")
                )
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

    private String getListingCodeClass(String language, ListingLine line) {
        String codeClass = null;

        if (language != null) {
            codeClass = language;
        }

        if (line.getLineChunks().get(0).isHighlight()) {
            if (codeClass == null) codeClass = "mark";
            else codeClass += " mark";
        }

        return codeClass;
    }


    @Override
    protected void endListing(ListingBlock listing) {
        append(PRE.end()).nl()
          .decIndent()
            .indent().append(DIV.end()).nl()
        ;
    }

}
