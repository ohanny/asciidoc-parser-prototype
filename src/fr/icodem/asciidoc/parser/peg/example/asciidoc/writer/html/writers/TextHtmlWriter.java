package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.Text;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public class TextHtmlWriter extends ModelHtmlWriter<TextHtmlWriter> {

    public TextHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Text text) {
        if (text.getInline() != null) {
            getInlineNodeWriter().write(text.getInline());
        } else if (text.getSource() != null) {
            getStringWriter().write(text.getSource());
        }
    }
}
