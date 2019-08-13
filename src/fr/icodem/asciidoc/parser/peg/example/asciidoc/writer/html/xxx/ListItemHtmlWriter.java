package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.Listing;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

import java.io.IOException;

public abstract class ListItemHtmlWriter extends ModelHtmlWriter<ListItemHtmlWriter> {

    public ListItemHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    public abstract void write(ListItem li) throws IOException;
}
