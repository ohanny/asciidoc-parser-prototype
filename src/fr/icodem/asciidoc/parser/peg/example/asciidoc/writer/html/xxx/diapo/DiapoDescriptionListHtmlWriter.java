package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.DescriptionList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.DescriptionListHtmlWriter;

import java.io.IOException;

public class DiapoDescriptionListHtmlWriter extends DescriptionListHtmlWriter {

    public DiapoDescriptionListHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    @Override
    public void write(DescriptionList list) throws IOException {

    }
}
