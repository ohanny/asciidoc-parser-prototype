package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.DescriptionListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class DescriptionListItemHtmlWriter extends ModelHtmlWriter<DescriptionListItemHtmlWriter> {

    public DescriptionListItemHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(DescriptionListItem item) {
        startItem(item);
        writeContent(item);
        endItem(item);
    }

    protected abstract void startItem(DescriptionListItem item);

    private void writeContent(DescriptionListItem item) {
        if (item.getText() != null) {
            startText(item);
            System.out.println("DescriptionListItemHtmlWriter : " +  item.getText().getInline());
            getTextWriter().write(item.getText());
            endText(item);
        }
        if (item.getBlocks() != null) {
            getBlockWriter().writeBlocks(item.getBlocks());
        }

    }

    protected abstract void startText(DescriptionListItem item);

    protected abstract void endText(DescriptionListItem item);

    protected abstract void endItem(DescriptionListItem item);
}
