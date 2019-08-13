package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Preamble;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

import java.io.IOException;

public abstract class PreambleHtmlWriter extends ModelHtmlWriter<PreambleHtmlWriter> {

    public PreambleHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    public abstract void write(Preamble preamble) throws IOException;
}
