package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ListBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.ListHtmlWriter;

import java.io.IOException;

public class DiapoListHtmlWriter extends ListHtmlWriter {

    public DiapoListHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    @Override
    public void write(ListBlock list) throws IOException {

    }
}
