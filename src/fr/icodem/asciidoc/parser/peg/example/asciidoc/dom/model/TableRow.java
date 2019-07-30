package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class TableRow extends Block {
    private List<TableCell> cells;

    public static TableRow of(List<TableCell> cells) {
        TableRow row = new TableRow();
        row.cells = cells;

        return row;
    }

    public List<TableCell> getCells() {
        return cells;
    }
}
