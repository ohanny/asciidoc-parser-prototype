package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

public class TableColumn extends Block {
    private double width;

    public static TableColumn of(double width) {
        TableColumn column = new TableColumn();
        column.width = width;

        return column;
    }

    public double getWidth() {
        return width;
    }
}
