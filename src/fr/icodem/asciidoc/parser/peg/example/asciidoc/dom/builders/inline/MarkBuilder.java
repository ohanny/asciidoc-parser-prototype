package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;

@Deprecated
public class MarkBuilder{
    private AttributeList attributeList;

    public static MarkBuilder of(AttributeList attList) {
        MarkBuilder builder = new MarkBuilder();
        builder.attributeList = attList;

        return builder;
    }

}
