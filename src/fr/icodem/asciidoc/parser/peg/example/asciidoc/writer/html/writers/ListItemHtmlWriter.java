package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class ListItemHtmlWriter extends ModelHtmlWriter<ListItemHtmlWriter> {

    public ListItemHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(ListItem li) {
        startListItem(li);
        writeContent(li);
        endListItem(li);
    }

    protected abstract void startListItem(ListItem li);

    private void writeContent(ListItem li) {
        if (li.getText() != null) {
            startText(li);
            getTextWriter().write(li.getText());
            endText(li);
        }
        if (li.getBlocks() != null) {
            writeBlocks(li.getBlocks());
        }
    }

    protected void startText(ListItem li) {}

    protected void endText(ListItem li) {}


    protected abstract void endListItem(ListItem li);
}
