package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Callout;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.CalloutHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.*;

public class DiapoCalloutHtmlWriter extends CalloutHtmlWriter {
    public DiapoCalloutHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    public void write(Callout callout) {
        String nb = Integer.toString(callout.getNumber());

        indent().append(TR.start()).nl().incIndent()
          .indent().append(TD.start()).append(I.start("class", "conum", "data-value", nb)).append(I.end())
          .append(B.start()).append(nb).append(B.end()).append(TD.end()).nl()
          .indent().append(TD.start()).append(callout.getText()).append(TD.end()).nl()
          .decIndent()
          .indent().append(TR.end()).nl()
        ;
    }

}
