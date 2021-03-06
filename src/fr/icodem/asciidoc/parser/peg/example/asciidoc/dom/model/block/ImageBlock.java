package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;

public class ImageBlock extends Block {
    private String source;
    private String alternateText;

    public static ImageBlock of(AttributeList attList, Title title, String source, String alternateText) {
        ImageBlock image = new ImageBlock();
        image.type = ElementType.ImageBlock;
        image.attributes = attList;
        image.title = title;
        image.source = source;
        image.alternateText = alternateText;

        return image;
    }

    public String getSource() {
        return source;
    }

    public String getAlternateText() {
        return alternateText;
    }
}
