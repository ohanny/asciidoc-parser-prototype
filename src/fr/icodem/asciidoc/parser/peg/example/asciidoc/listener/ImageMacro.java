package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class ImageMacro extends Macro {
    ImageMacro() {}

    public String getAlternateText() {
        String alt = (attributeList== null)?null:attributeList.getFirstPositionalAttribute();
        if (alt == null) {
            int beginIndex = target.lastIndexOf('/');
            if (beginIndex == -1) beginIndex = 0;

            int endIndex = target.lastIndexOf('.');
            if (endIndex == -1) endIndex = target.length() - 1;

            alt = target.substring(beginIndex, endIndex);
        }

        return alt;
    }
}
