package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.RevisionInfo;

public class RevisionInfoBuilder {

    private String date;
    private String number;
    private String remark;

    public static RevisionInfoBuilder newBuilder() {
        return new RevisionInfoBuilder();
    }

    public RevisionInfo build() {
        return RevisionInfo.of(date.trim(), number, remark); // TODO reprendre le trim
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
