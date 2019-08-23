package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.backend.html.HtmlTag;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ListBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.ListHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.*;

public class DiapoOrderedListHtmlWriter extends ListHtmlWriter {

    public DiapoOrderedListHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startList(ListBlock list) {
        String style = styleBuilder().reset(list.getAttributes()).addPosition().addSize().style();

        indent().append(DIV.start("class", "olist", "style", style)).nl()
          .incIndent()
          .appendIf(list.getLevel() == 1, () -> writeBlockTitle(list))
          .indent().append(UL.start("style", style)).nl()
          .incIndent()
        ;
    }

    @Override
    protected void endList(ListBlock list) {
        decIndent()
          .indent().append(OL.end()).nl()
          .decIndent()
          .indent().append(HtmlTag.DIV.end()).nl()
        ;
    }

}
