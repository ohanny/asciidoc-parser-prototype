package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Section;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.SectionHtmlWriter;

import java.io.IOException;

public class DiapoSectionHtmlWriter extends SectionHtmlWriter {

    public DiapoSectionHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    @Override
    protected void start(Section section) throws IOException {

    }

    @Override
    protected void end(Section section) throws IOException {

    }
}
