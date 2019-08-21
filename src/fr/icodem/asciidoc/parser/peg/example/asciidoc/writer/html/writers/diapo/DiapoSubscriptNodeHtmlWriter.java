package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.DecoratorNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.DecoratorNodeHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.SUB;

public class DiapoSubscriptNodeHtmlWriter extends DecoratorNodeHtmlWriter {
    public DiapoSubscriptNodeHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startNode(DecoratorNode node) {
        append(SUB.start());
    }

    @Override
    protected void endNode(DecoratorNode node) {
        append(SUB.end());
    }

}
