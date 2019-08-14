package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class ImageBlock extends Block {
    private String source;
    private String alternateText;

    public static ImageBlock of(AttributeList attList, String source, String alternateText) {
        ImageBlock image = new ImageBlock();
        image.type = ElementType.ImageBlock;
        image.attributes = attList;
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
