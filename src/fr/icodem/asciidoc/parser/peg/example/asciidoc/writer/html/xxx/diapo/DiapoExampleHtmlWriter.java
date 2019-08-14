package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ExampleBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.ExampleHtmlWriter;

import java.io.IOException;

public class DiapoExampleHtmlWriter extends ExampleHtmlWriter {

    public DiapoExampleHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    public void write(ExampleBlock example) throws IOException {

    }
}
