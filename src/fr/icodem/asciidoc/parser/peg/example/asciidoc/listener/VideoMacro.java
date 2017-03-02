package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class VideoMacro extends Macro {
    VideoMacro() {}

    public String getTitle() {
        String title = (attributes== null)?null:attributes.getStringValue("title", null);

        return title;
    }
}
