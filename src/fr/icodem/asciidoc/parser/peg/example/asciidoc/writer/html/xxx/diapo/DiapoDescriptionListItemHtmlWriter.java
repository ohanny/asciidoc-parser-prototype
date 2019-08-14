package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.DescriptionListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.DescriptionListItemHtmlWriter;

import java.io.IOException;

public class DiapoDescriptionListItemHtmlWriter extends DescriptionListItemHtmlWriter {

    public DiapoDescriptionListItemHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    public void write(DescriptionListItem li) throws IOException {

    }
}
