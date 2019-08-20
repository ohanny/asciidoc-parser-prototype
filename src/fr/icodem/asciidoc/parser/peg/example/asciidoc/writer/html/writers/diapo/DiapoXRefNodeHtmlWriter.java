package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.XRefNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.XRefNodeHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.A;

public class DiapoXRefNodeHtmlWriter extends XRefNodeHtmlWriter {
    public DiapoXRefNodeHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    public void write(XRefNode xref) {
        String href = xref.isInternal()?"#" + xref.getValue():xref.getValue();

        append(A.start("href", href))
                .append(xref.getLabel())
                .append(A.end());

    }


}
