package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.DecoratorNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.DecoratorNodeHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.EM;

public class DiapoItalicNodeHtmlWriter extends DecoratorNodeHtmlWriter {
    public DiapoItalicNodeHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startNode(DecoratorNode node) {
        append(EM.start());
    }

    @Override
    protected void endNode(DecoratorNode node) {
        append(EM.end());
    }

}
