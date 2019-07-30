package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.TableColumn;

public class TableColumnBuilder implements BlockBuilder {

    public static TableColumnBuilder empty() {
        TableColumnBuilder column = new TableColumnBuilder();
        return column;
    }

    @Override
    public TableColumn build() {
        return null;
    }
}
