package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ImageBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.VideoBlock;

public class BlockMacroBuilder implements BlockBuilder {
    private AttributeEntries entries;
    private AttributeList attributes;
    private String title;
    private String name;
    private String target;

    public static BlockMacroBuilder of(AttributeEntries entries, AttributeList attList, String title) {
        BlockMacroBuilder builder = new BlockMacroBuilder();
        builder.entries = entries;
        builder.attributes = attList;
        builder.title = title;

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
        return ImageBlock.of(attributes, Title.of(title), target, getAlternateText());
    }

    private VideoBlock buildVideo() {
        String path = entries.getValue("videosdir");
        if (path != null) {
            target = path + "/" + target;
        }
        return VideoBlock.of(attributes, Title.of(title), target, getAlternateText());
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
