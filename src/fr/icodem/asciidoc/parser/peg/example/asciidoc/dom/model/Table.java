package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class Table extends Block {
    private List<TableColumn> columns;
    private List<TableRow> header;
    private List<TableRow> footer;
    private List<TableRow> body;

    public static Table of(List<TableColumn> columns, List<TableRow> header, List<TableRow> footer, List<TableRow> body) {
        Table table = new Table();
        table.type = ElementType.Table;
        table.columns = columns;
        table.header = header;
        table.footer = footer;
        table.body = body;

        return table;
    }

    public List<TableColumn> getColumns() {
        return columns;
    }

    public List<TableRow> getHeader() {
        return header;
    }

    public List<TableRow> getFooter() {
        return footer;
    }

    public List<TableRow> getBody() {
        return body;
    }
}
