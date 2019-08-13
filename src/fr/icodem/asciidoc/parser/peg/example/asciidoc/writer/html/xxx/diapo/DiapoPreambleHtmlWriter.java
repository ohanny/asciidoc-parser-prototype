package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Preamble;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.PreambleHtmlWriter;

import java.io.IOException;

public class DiapoPreambleHtmlWriter extends PreambleHtmlWriter {

    public DiapoPreambleHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    @Override
    public void write(Preamble preamble) throws IOException {

    }
}
