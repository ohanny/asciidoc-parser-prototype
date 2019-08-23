package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.BlockTitleHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.DIV;

public class DiapoBlockTitleHtmlWriter extends BlockTitleHtmlWriter {

    public DiapoBlockTitleHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    public void write(Title title) {
        indent().append(DIV.start("class", "title"));

        if (title.getInline() != null) {
            getInlineNodeWriter().write(title.getInline());
        } else {
            append(title.getText());
        }

        append(DIV.end()).nl();
    }
}
