package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.TableCell;

public class TableCellBuilder implements BlockBuilder {
    private String text;
    private TableCellBuilder next;

    public static TableCellBuilder empty() {
        TableCellBuilder cell = new TableCellBuilder();
        return cell;
    }

    public static TableCellBuilder withParent(TableCellBuilder parent) {
        TableCellBuilder cell = TableCellBuilder.empty();
        parent.next = cell;
        return cell;
    }

    @Override
    public TableCell build() {
        return null;
    }
}
