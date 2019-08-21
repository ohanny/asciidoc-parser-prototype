package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Callout;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.util.List;

public abstract class CalloutsHtmlWriter extends ModelHtmlWriter<CalloutsHtmlWriter> {

    public CalloutsHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(List<Callout> callouts) {
        startCallouts();
        writeContent(callouts);
        endCallouts();
    }

    protected abstract void startCallouts();

    private void writeContent(List<Callout> callouts) {
        callouts.forEach(getCalloutWriter()::write);
    }

    protected abstract void endCallouts();


}
