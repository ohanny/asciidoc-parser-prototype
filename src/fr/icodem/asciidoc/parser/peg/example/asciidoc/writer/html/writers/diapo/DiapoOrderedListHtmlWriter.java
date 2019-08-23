package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ListBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.ListHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.OL;

public class DiapoOrderedListHtmlWriter extends ListHtmlWriter {

    public DiapoOrderedListHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startList(ListBlock list) {
        indent().append(OL.start("style", styleBuilder().reset(list.getAttributes()).addPosition().addSize().style())).nl()
          .incIndent()
        ;
    }

    @Override
    protected void endList(ListBlock list) {
        decIndent()
          .indent().append(OL.end()).nl()
        ;
    }

}
