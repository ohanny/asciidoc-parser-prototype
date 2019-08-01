package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.TableColumn;

public class TableColumnBuilder implements BlockBuilder {
    private double width;

    public static TableColumnBuilder newBuilder() {
        TableColumnBuilder column = new TableColumnBuilder();
        return column;
    }

    @Override
    public TableColumn build() {
        return TableColumn.of(width);
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
