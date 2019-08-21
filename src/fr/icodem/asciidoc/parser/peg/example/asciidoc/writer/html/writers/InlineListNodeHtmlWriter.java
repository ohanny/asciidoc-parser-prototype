package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineListNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public class InlineListNodeHtmlWriter extends ModelHtmlWriter<InlineListNodeHtmlWriter> {

    public InlineListNodeHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(InlineListNode node) {
        node.getNodes().forEach(getInlineNodeWriter()::write);
    }

}
