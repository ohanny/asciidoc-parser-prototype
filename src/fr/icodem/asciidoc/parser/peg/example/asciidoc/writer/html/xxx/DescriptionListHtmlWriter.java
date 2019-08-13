package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.DescriptionList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Section;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

import java.io.IOException;

public abstract class DescriptionListHtmlWriter extends ModelHtmlWriter<DescriptionListHtmlWriter> {

    public DescriptionListHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    public abstract void write(DescriptionList list) throws IOException;
}
