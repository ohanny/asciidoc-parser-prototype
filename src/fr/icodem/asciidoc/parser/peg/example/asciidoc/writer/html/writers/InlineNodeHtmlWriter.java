package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public class InlineNodeHtmlWriter extends ModelHtmlWriter<InlineNodeHtmlWriter> {

    public InlineNodeHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    protected void write(InlineNode node) {
        switch (node.getType()) {
            case InlineListNode:
                getInlineListNodeWriter().write((InlineListNode) node);
                break;

            case TextNode:
                getTextNodeWriter().write((TextNode) node);
                break;

            case BoldNode:
                getBoldNodeWriter().write((DecoratorNode) node);
                break;

            case ItalicNode:
                getItalicNodeWriter().write((DecoratorNode) node);
                break;

            case SuperscriptNode:
                getSuperscriptNodeWriter().write((DecoratorNode) node);
                break;

            case SubscriptNode:
                getSubscriptNodeWriter().write((DecoratorNode) node);
                break;

            case MonospaceNode:
                getMonospaceNodeWriter().write((DecoratorNode) node);
                break;

            case MarkNode:
                getMarkNodeWriter().write((DecoratorNode) node);
                break;

            case XRefNode:
                getXRefNodeWriter().write((XRefNode) node);
                break;

        }

    }
}
