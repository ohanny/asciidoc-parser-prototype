package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.DescriptionListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.DescriptionListItemHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.*;

public class DiapoDescriptionListItemHtmlWriter extends DescriptionListItemHtmlWriter {

    public DiapoDescriptionListItemHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startItem(DescriptionListItem item) {
        indent().append(DT.start("class", "hdlist1"))
          .append(item.getTitle().getText())
          .append(DT.end()).nl()
          .indent().append(DD.start())
          .appendIf(item.hasBlocks(), () -> nl().incIndent());
        ;
    }

    @Override
    protected void startText(DescriptionListItem item) {
        appendIf(item.hasBlocks(), () -> indent().append(P.start()));
    }

    @Override
    protected void endText(DescriptionListItem item) {
        appendIf(item.hasBlocks(), () -> append(P.end()).nl());
    }

    @Override
    protected void endItem(DescriptionListItem item) {
        appendIf(item.hasBlocks(), () -> decIndent().indent())
          .append(DD.end()).nl()
        ;
    }

}
