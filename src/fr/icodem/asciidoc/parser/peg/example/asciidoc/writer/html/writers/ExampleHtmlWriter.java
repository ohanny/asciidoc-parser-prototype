package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ExampleBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class ExampleHtmlWriter extends ModelHtmlWriter<ExampleHtmlWriter> {

    public ExampleHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(ExampleBlock example) {
        startExample(example);
        writeContent(example);
        endExample(example);
    }


    protected abstract void startExample(ExampleBlock example);

    private void writeContent(ExampleBlock example) {
        getBlockWriter().writeBlocks(example.getBlocks());
    }

    protected abstract void endExample(ExampleBlock example);

}
