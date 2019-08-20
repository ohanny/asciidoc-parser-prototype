package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Preamble;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;

public abstract class PreambleHtmlWriter <DHW extends PreambleHtmlWriter<DHW>> extends ModelHtmlWriter<DHW> {

    public PreambleHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Preamble preamble) throws IOException {
        startPreamble(preamble);
        writePreamble(preamble);
        endPreamble(preamble);
    }

    protected void writePreamble(Preamble preamble) throws IOException {
        getBlockWriter().writeBlocks(preamble.getBlocks());
    }

    protected abstract void startPreamble(Preamble preamble) throws IOException;

    protected abstract void endPreamble(Preamble preamble) throws IOException;
}
