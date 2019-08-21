package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Callout;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class CalloutHtmlWriter extends ModelHtmlWriter<CalloutHtmlWriter> {

    public CalloutHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public abstract void write(Callout callout);


}
