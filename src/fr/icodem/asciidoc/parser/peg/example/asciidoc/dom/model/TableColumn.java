package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class TableColumn extends Block {

    public static TableColumn of() {
        TableColumn column = new TableColumn();

        return column;
    }
}
