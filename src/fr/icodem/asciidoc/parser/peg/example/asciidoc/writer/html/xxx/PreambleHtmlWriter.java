package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

import java.io.IOException;
import java.util.List;

public abstract class PreambleHtmlWriter <DHW extends PreambleHtmlWriter<DHW>> extends ModelHtmlWriter<DHW> {

    public PreambleHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    public void write(Preamble preamble) throws IOException {
        startPreamble(preamble);
        writePreamble(preamble);
        endPreamble(preamble);
    }

    protected void writePreamble(Preamble preamble) throws IOException {
        writeBlocks(preamble.getBlocks());
    }

    protected abstract void startPreamble(Preamble preamble) throws IOException;

    protected abstract void endPreamble(Preamble preamble) throws IOException;
}
