package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableCell;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableRow;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TableRowBuilder implements BlockBuilder {

    private boolean header;
    private boolean footer;
    private Deque<TableCellBuilder> cells;

    public static TableRowBuilder newBuilder() {
        TableRowBuilder builder = new TableRowBuilder();
        builder.cells = new LinkedList<>();

        return builder;
    }

    @Override
    public TableRow build() {
        List<TableCell> cells = this.cells
                .stream()
                .map(TableCellBuilder::build)
                .collect(Collectors.toList());

        return TableRow.of(cells);
    }

    public void addCell() {
        this.cells.add(TableCellBuilder.newBuilder());
    }

    private TableCellBuilder getLastCell() {
        return cells.peekLast();
    }

    public void setContent(String content) {
        getLastCell().setText(content);
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public boolean isFooter() {
        return footer;
    }

    public void setFooter(boolean footer) {
        this.footer = footer;
    }

    public boolean isBody() {
        return !header && !footer;
    }

}
