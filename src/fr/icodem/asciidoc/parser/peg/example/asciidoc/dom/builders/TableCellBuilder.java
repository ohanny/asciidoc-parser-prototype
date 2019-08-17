package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableCell;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.Text;

public class TableCellBuilder implements BlockBuilder {
    private String content;

    public static TableCellBuilder newBuilder() {
        TableCellBuilder cell = new TableCellBuilder();
        return cell;
    }

    @Override
    public TableCell build() {
        return TableCell.of(Text.of(content), 0, 0);
    }

    public void setContent(String content) {
        this.content = content;
    }
}
