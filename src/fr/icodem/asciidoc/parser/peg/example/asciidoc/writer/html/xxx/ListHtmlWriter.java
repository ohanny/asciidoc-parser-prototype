package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ListBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class ListHtmlWriter extends ModelHtmlWriter<ListHtmlWriter> {

    public ListHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(ListBlock list) {
        startList(list);
        writeContent(list);
        endList(list);
    }

    protected abstract void startList(ListBlock list);

    private void writeContent(ListBlock list) {
        for (ListItem item : list.getItems()) {
            getListItemWriter().write(item);
        }
    }

    protected abstract void endList(ListBlock list);
}
