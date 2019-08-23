package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ImageBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.VideoBlock;

public class BlockMacroBuilder implements BlockBuilder {
    private AttributeEntries entries;
    private AttributeList attributes;
    private TitleBuilder title;
    private String name;
    private String target;

    public static BlockMacroBuilder of(BlockBuildState state, AttributeEntries entries, AttributeList attList) {
        BlockMacroBuilder builder = new BlockMacroBuilder();
        builder.entries = entries;
        builder.attributes = attList;
        builder.title = state.consumeBlockTitle();

        return builder;
    }

    @Override
    public Block build() {
        switch (name) {
            case "image":
                return buildImage();
            case "video":
                return buildVideo();
        }
        return null;
    }

    private ImageBlock buildImage() {
        String path = entries.getValue("imagesdir");
        if (path != null) {
            target = path + "/" + target;
        }
        return ImageBlock.of(attributes, buildTitle(title), target, getAlternateText());
    }

    private VideoBlock buildVideo() {
        String path = entries.getValue("videosdir");
        if (path != null) {
            target = path + "/" + target;
        }
        return VideoBlock.of(attributes, buildTitle(title), target, getAlternateText());
    }

    private String getAlternateText() {
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


    public boolean isBlock() {
        return "image".equals(name) || "video".equals(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
