package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.LiteralHtmlWriter;
import sun.tools.jstat.Literal;

import java.io.IOException;

public class DiapoLiteralHtmlWriter extends LiteralHtmlWriter {

    public DiapoLiteralHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    @Override
    public void write(Literal literal) throws IOException {

    }
}
