package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;

public class StyleAttributeBuilder {
    private StringBuilder style = new StringBuilder();
    private AttributeList attributeList;

    public static StyleAttributeBuilder newIntance() {
        return new StyleAttributeBuilder();
    }

    public String style() {
        return style.length() == 0?null:style.toString();
    }

    public StyleAttributeBuilder reset(AttributeList attList) {
        this.attributeList = attList;
        style.setLength(0);
        return this;
    }

    public StyleAttributeBuilder addPosition() {
        if (attributeList != null) {
            String top = attributeList.getStringValue("top", null);
            String right = attributeList.getStringValue("right", null);
            String bottom = attributeList.getStringValue("bottom", null);
            String left = attributeList.getStringValue("left", null);


            if (top != null || right != null || bottom != null || left != null) {
                String pos = attributeList.getStringValue("position", "absolute");
                style.append("position: ").append(pos).append(";");
                //style.append("position: absolute;");

                if (top != null) {
                    style.append("top: " + top + "px;");
                }
                if (right != null) {
                    style.append("right: " + right + "px;");
                }
                if (bottom != null) {
                    style.append("bottom: " + bottom + "px;");
                }
                if (left != null) {
                    style.append("left: " + left + "px;");
                }
            }

        }

        return this;
    }

    public StyleAttributeBuilder addSize() {
        if (attributeList != null) {
            String width = attributeList.getStringValue("width", null);
            String height = attributeList.getStringValue("height", null);


            if (width != null) {
                style.append("width: " + width + "px;");
            }
            if (height != null) {
                style.append("height: " + height + "px;");
            }

        }

        return this;
    }

}
