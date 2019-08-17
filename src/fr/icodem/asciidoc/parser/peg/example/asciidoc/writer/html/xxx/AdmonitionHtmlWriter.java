package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ExampleBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Paragraph;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;

public abstract class AdmonitionHtmlWriter extends ModelHtmlWriter<AdmonitionHtmlWriter> {

    public AdmonitionHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Paragraph p) {
        startAdmonition(p.getAdmonition(), p);
        writeContent(p);
        endAdmonition(p.getAdmonition(), p);
    }

    public void write(ExampleBlock example) {
        startAdmonition(example.getAdmonition(), example);
        writeContent(example);
        endAdmonition(example.getAdmonition(), example);
    }

    protected abstract void startAdmonition(String admonition, Block block);

    private void writeContent(Paragraph p) {
        getTextWriter().write(p.getText());
    }

    private void writeContent(ExampleBlock example) {
        writeBlocks(example.getBlocks());
    }

    protected abstract void endAdmonition(String admonition, Block block);
}
