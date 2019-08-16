package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.LiteralBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;

public abstract class LiteralHtmlWriter extends ModelHtmlWriter<LiteralHtmlWriter> {

    public LiteralHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(LiteralBlock literal) throws IOException {
        startLiteral(literal);
        writeContent(literal);
        endLiteral(literal);
    }

    protected abstract void startLiteral(LiteralBlock literal);

    protected abstract void writeContent(LiteralBlock literal) throws IOException;

    protected abstract void endLiteral(LiteralBlock literal);
}
