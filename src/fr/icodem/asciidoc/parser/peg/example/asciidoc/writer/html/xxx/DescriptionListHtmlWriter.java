package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.DescriptionList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.DescriptionListItem;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class DescriptionListHtmlWriter extends ModelHtmlWriter<DescriptionListHtmlWriter> {

    public DescriptionListHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(DescriptionList list) {
        startDescriptionList(list);
        writeContent(list);
        endDescriptionList(list);
    }

    protected abstract void startDescriptionList(DescriptionList list);

    private void writeContent(DescriptionList list) {
        for (DescriptionListItem item : list.getItems()) {
            getDescriptionListItemWriter().write(item);
        }
    }

    protected abstract void endDescriptionList(DescriptionList list);
}
