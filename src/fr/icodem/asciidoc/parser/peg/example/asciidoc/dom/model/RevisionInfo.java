package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class RevisionInfo {
    private String date;
    private String number;
    private String remark;


    public static RevisionInfo of(String date, String number, String remark) {
        RevisionInfo info = new RevisionInfo();
        info.date = date;
        info.number = number;
        info.remark = remark;

        return info;
    }

    public String getDate() {
        return date;
    }

    public String getNumber() {
        return number;
    }

    public String getRemark() {
        return remark;
    }
}
