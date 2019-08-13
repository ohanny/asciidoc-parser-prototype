package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Header;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.HeaderHtmlWriter;

import java.io.IOException;

public class DiapoHeaderHtmlWriter extends HeaderHtmlWriter {

    public DiapoHeaderHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    @Override
    public void write(Header header) throws IOException {

    }
}
