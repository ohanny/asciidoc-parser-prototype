package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Table;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableColumn;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.TableHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.*;

public class DiapoTableHtmlWriter extends TableHtmlWriter {

    public DiapoTableHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startTable(Table table) {
        String cssClass = "tableblock frame-all grid-all";
        if (table.getAttributes() == null || !table.getAttributes().hasOption("autowidth")) {
            cssClass += " spread";
        }

        indent().append(TABLE.start("class", cssClass)).nl()
          .incIndent()
        ;
    }

    @Override
    protected void endTable(Table table) {
        decIndent()
          .indent().append(TABLE.end()).nl()
        ;
    }

    @Override
    protected void startColumns(Table table) {
        indent().append(COLGROUP.start()).nl()
          .incIndent()
        ;
    }

    @Override
    protected void writeColumn(TableColumn column) {
        StringBuilder sb = new StringBuilder();
        AttributeList attList = column.getTable().getAttributes();
        if (attList == null || !attList.hasOption("autowidth")) {
            sb.append("width: " + column.getWidth() + "%;");
        }
        String style = sb.toString();

        indent()
          .appendIf(style.length() > 0, () -> append(COL.tag("style", style)))
          .appendIf(style.length() == 0, () -> append(COL.tag()))
          .nl()
        ;
    }

    @Override
    protected void endColumns(Table table) {
        decIndent()
          .indent().append(COLGROUP.end()).nl()
        ;
    }

    @Override
    protected void startHeader(Table table) {
        indent().append(THEAD.start()).nl()
          .incIndent()
        ;
    }

    @Override
    protected void endHeader(Table table) {
        decIndent()
          .indent().append(THEAD.end()).nl()
        ;
    }

    @Override
    protected void startBody(Table table) {
        indent().append(TBODY.start()).nl()
          .incIndent()
        ;
    }

    @Override
    protected void endBody(Table table) {
        decIndent()
          .indent().append(TBODY.end()).nl()
        ;
    }

    @Override
    protected void startFooter(Table table) {
        indent().append(TFOOT.start()).nl()
          .incIndent()
        ;
    }

    @Override
    protected void endFooter(Table table) {
        decIndent()
          .indent().append(TFOOT.end()).nl()
        ;
    }

}
