package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;

public class VideoBlock extends Block {
    private String source;
    private String alternateText;

    public static VideoBlock of(AttributeList attList, Title title, String source, String alternateText) {
        VideoBlock video = new VideoBlock();
        video.type = ElementType.Video;
        video.attributes = attList;
        video.title = title;
        video.source = source;
        video.alternateText = alternateText;

        return video;
    }

    public String getSource() {
        return source;
    }

    public String getAlternateText() {
        return alternateText;
    }
}
