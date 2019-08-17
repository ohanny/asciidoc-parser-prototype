package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ListBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.ListItemHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.LI;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.P;

public class DiapoListItemHtmlWriter extends ListItemHtmlWriter {

    public DiapoListItemHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startListItem(ListItem li) {
        ListBlock parent = li.getParent();
        boolean addNextClass = parent.getAttributes() != null &&
                parent.getAttributes().hasOption("step") &&
                li.getPosition() > 0 && parent.getLevel() == 1;

        indent()
          .appendIf(addNextClass, () -> append(LI.start("class", "next")))
          .appendIf(!addNextClass, () -> append(LI.start()))
          .appendIf(li.hasBlocks(), () -> nl().incIndent())
        ;
    }

    @Override
    protected void startText(ListItem li) {
        appendIf(li.hasBlocks(), () -> indent().append(P.start()));
    }

    @Override
    protected void endText(ListItem li) {
        appendIf(li.hasBlocks(), () -> append(P.end()).nl());
    }

    @Override
    protected void endListItem(ListItem li) {
        appendIf(li.hasBlocks(), () -> decIndent().indent())
          .append(LI.end()).nl()
        ;
    }

}
