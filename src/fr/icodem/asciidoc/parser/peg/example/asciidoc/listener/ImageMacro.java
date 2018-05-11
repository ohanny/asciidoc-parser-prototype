package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class ImageMacro extends Macro {

    private String title;

    ImageMacro() {}

    public String getAlternateText() {
        String alt = (attributes== null)?null:attributes.getFirstPositionalAttribute();
        if (alt == null) {
            int beginIndex = target.lastIndexOf('/');
            if (beginIndex == -1) beginIndex = 0;

            int endIndex = target.lastIndexOf('.');
            if (endIndex == -1) endIndex = target.length() - 1;

            alt = target.substring(beginIndex, endIndex);
        }

        return alt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
