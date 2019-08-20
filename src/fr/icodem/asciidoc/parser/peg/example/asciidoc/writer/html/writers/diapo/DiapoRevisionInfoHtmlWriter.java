package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.RevisionInfo;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.RevisionInfoHtmlWriter;

import java.io.IOException;

public class DiapoRevisionInfoHtmlWriter extends RevisionInfoHtmlWriter {

    public DiapoRevisionInfoHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    public void write(RevisionInfo info) throws IOException {

    }
}
