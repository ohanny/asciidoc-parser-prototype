package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.DescriptionList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class DescriptionListHtmlWriter extends ModelHtmlWriter<DescriptionListHtmlWriter> {

    public DescriptionListHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public abstract void write(DescriptionList list);
}
