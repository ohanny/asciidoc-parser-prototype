package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ListingBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class ListingHtmlWriter extends ModelHtmlWriter<ListingHtmlWriter> {

    public ListingHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(ListingBlock listing) {
        startListing(listing);
        writeContent(listing);
        endListing(listing);
    }

    protected abstract void startListing(ListingBlock listing);

    private void writeContent(ListingBlock listing) {
    }

    protected abstract void endListing(ListingBlock listing);
}
