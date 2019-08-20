package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.TextNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public class TextNodeHtmlWriter extends ModelHtmlWriter<TextNodeHtmlWriter> {

    public TextNodeHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(TextNode node) {
        getStringWriter().write(node.getText());
    }
}
