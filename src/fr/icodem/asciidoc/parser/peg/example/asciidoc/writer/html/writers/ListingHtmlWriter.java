package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.ListingBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class ListingHtmlWriter <DHW extends ListingHtmlWriter<DHW>> extends ModelHtmlWriter<DHW> {

    public ListingHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(ListingBlock listing) {
        startListing(listing);
        writeContent(listing);
        endListing(listing);
        writeCallouts(listing);
    }

    protected abstract void startListing(ListingBlock listing);

    private void writeContent(ListingBlock listing) {
        if (listing.getText() != null) append(listing.getText().getSource());
    }

    protected abstract void endListing(ListingBlock listing);

    private void writeCallouts(ListingBlock listing) {
        if (listing.getCallouts() != null) {
            getCalloutsWriter().write(listing.getCallouts());
        }
    }

}
