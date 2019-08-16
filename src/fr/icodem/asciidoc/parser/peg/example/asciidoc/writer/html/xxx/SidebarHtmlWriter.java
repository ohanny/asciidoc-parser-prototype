package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Sidebar;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;

public abstract class SidebarHtmlWriter extends ModelHtmlWriter<SidebarHtmlWriter> {

    public SidebarHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Sidebar sidebar) throws IOException {
        startSidebar(sidebar);
        writeContent(sidebar);
        endSidebar(sidebar);
    }

    protected abstract void startSidebar(Sidebar sidebar);

    private void writeContent(Sidebar sidebar) throws IOException {
        writeBlocks(sidebar.getBlocks());
    }

    protected abstract void endSidebar(Sidebar sidebar);
}