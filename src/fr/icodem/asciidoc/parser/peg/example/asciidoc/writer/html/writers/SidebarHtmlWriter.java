package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Sidebar;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class SidebarHtmlWriter extends ModelHtmlWriter<SidebarHtmlWriter> {

    public SidebarHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Sidebar sidebar) {
        startSidebar(sidebar);
        writeContent(sidebar);
        endSidebar(sidebar);
    }

    protected abstract void startSidebar(Sidebar sidebar);

    private void writeContent(Sidebar sidebar) {
        getBlockWriter().writeBlocks(sidebar.getBlocks());
    }

    protected abstract void endSidebar(Sidebar sidebar);
}
