package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.RevisionInfo;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.RevisionInfoHtmlWriter;

import java.io.IOException;

public class DiapoRevisionInfoHtmlWriter extends RevisionInfoHtmlWriter {

    public DiapoRevisionInfoHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    @Override
    public void write(RevisionInfo info) throws IOException {

    }
}
