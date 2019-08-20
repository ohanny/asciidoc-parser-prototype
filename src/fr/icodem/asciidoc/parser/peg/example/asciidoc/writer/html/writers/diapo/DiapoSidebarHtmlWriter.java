package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Sidebar;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.SidebarHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.DIV;

public class DiapoSidebarHtmlWriter extends SidebarHtmlWriter {

    public DiapoSidebarHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startSidebar(Sidebar sidebar) {
        String classes = getMoreClasses("sidebarblock", sidebar.getAttributes());
        indent().append(DIV.start("class", classes, "style",
                    styleBuilder().reset(sidebar.getAttributes()).addPosition().addSize().style())).nl()
          .incIndent()
            .indent().append(DIV.start("class", "content")).incIndent().nl()
            .writeBlockTitle(sidebar)
        ;
    }

    @Override
    protected void endSidebar(Sidebar sidebar) {
        decIndent()
          .indent().append(DIV.end()).nl()
          .decIndent()
          .indent().append(DIV.end()).nl()
        ;
    }

}
