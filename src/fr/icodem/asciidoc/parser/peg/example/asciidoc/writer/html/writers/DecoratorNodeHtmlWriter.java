package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.DecoratorNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class DecoratorNodeHtmlWriter extends ModelHtmlWriter<DecoratorNodeHtmlWriter> {

    public DecoratorNodeHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(DecoratorNode node) {
        startNode(node);
        writeContent(node);
        endNode(node);
    }

    protected abstract void startNode(DecoratorNode node);

    private void writeContent(DecoratorNode node) {
        getInlineNodeWriter().write(node.getNode());
    }

    protected abstract void endNode(DecoratorNode node);

}
