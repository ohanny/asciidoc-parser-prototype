package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class Quote extends TextBlock {
    private String attribution;
    private String citationTitle;

    public static Quote of(AttributeList attList, Text text, String attribution, String citationTitle) {
        Quote quote = new Quote();
        quote.attribution = attribution;
        quote.citationTitle = citationTitle;
        quote.attributeList = attList;
        quote.text = text;

        return quote;
    }

    public String getAttribution() {
        return attribution;
    }

    public String getCitationTitle() {
        return citationTitle;
    }
}
