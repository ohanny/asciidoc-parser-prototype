package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Section;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

import java.io.IOException;

public abstract class SectionHtmlWriter extends ModelHtmlWriter<SectionHtmlWriter> {

    public SectionHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    public void write(Section section) throws IOException {
        start(section);

        end(section);
    }

    protected abstract void start(Section section) throws IOException;

    protected abstract void end(Section section) throws IOException;
}
