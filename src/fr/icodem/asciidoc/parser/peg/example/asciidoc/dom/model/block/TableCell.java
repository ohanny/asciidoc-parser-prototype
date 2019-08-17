package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.Text;

public class TableCell extends TextBlock {
    private int colspan;
    private int rowspan;

    public static TableCell of(Text text, int colspan, int rowspan) {
        TableCell cell = new TableCell();
        cell.text = text;
        cell.colspan = colspan;
        cell.rowspan = rowspan;

        return cell;
    }

    public int getColspan() {
        return colspan;
    }

    public int getRowspan() {
        return rowspan;
    }
}
