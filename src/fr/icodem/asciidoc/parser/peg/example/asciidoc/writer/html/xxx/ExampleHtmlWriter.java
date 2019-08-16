package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ExampleBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;

public abstract class ExampleHtmlWriter extends ModelHtmlWriter<ExampleHtmlWriter> {

    public ExampleHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(ExampleBlock example) throws IOException {
        startExample(example);
        writeContent(example);
        endExample(example);
    }


    protected abstract void startExample(ExampleBlock example);

    private void writeContent(ExampleBlock example) throws IOException {
        writeBlocks(example.getBlocks());
    }

    protected abstract void endExample(ExampleBlock example);

}
