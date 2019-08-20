package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.DecoratorNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.DecoratorNodeHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.EM;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.SUP;

public class DiapoSuperscriptNodeHtmlWriter extends DecoratorNodeHtmlWriter {
    public DiapoSuperscriptNodeHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startNode(DecoratorNode node) {
        append(SUP.start());
    }

    @Override
    protected void endNode(DecoratorNode node) {
        append(SUP.end());
    }

}
