package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ExampleBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.ExampleHtmlWriter;

import java.io.IOException;

public class DiapoExampleHtmlWriter extends ExampleHtmlWriter {

    public DiapoExampleHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    @Override
    public void write(ExampleBlock example) throws IOException {

    }
}
