package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

public class TableColumn extends Block {
    private double width;
    private Table table;

    public static TableColumn of(double width) {
        TableColumn column = new TableColumn();
        column.width = width;

        return column;
    }

    public double getWidth() {
        return width;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
