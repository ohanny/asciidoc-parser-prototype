package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class Macro {
    protected String name;
    protected String target;

    protected AttributeList attributeList;

    Macro() {}

    public static Macro macro(String name, String target, AttributeList attributeList) {
        Macro macro = new Macro();
        macro.name = name;
        macro.target = target;
        macro.attributeList = attributeList;

        return macro;
    }

    public static ImageMacro image(String name, String target, AttributeList attributeList) {
        ImageMacro macro = new ImageMacro();
        macro.name = name;
        macro.target = target;
        macro.attributeList = attributeList;

        return macro;
    }

    public String getName() {
        return name;
    }

    public String getTarget() {
        return target;
    }

    public AttributeList getAttributeList() {
        return attributeList;
    }

}
