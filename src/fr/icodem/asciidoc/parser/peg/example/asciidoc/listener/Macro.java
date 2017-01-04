package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class Macro {
    protected String name;
    protected String target;

    protected AttributeList blockAttributes;
    protected AttributeList attributes;

    Macro() {}

    public static Macro macro(String name, String target,
                              AttributeList attributes, AttributeList blockAttributes) {
        Macro macro = new Macro();
        macro.name = name;
        macro.target = target;
        macro.attributes = attributes;
        macro.blockAttributes = blockAttributes;

        return macro;
    }

    public static ImageMacro image(String name, String target,
                                   AttributeList attributes, AttributeList blockAttributes) {
        ImageMacro macro = new ImageMacro();
        macro.name = name;
        macro.target = target;
        macro.attributes = attributes;
        macro.blockAttributes = blockAttributes;

        return macro;
    }

    public String getName() {
        return name;
    }

    public String getTarget() {
        return target;
    }

    public AttributeList getAttributes() {
        return attributes;
    }

    public AttributeList getBlockAttributes() {
        return blockAttributes;
    }
}
