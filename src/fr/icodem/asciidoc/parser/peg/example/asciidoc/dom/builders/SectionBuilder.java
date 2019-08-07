package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Section;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Title;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SectionBuilder implements BlockContainer {
    private int level;
    private String title;
    private String ref;
    private AttributeList attList;

    private SectionBuilder next;
    private SectionBuilder previous; // TODO to be removed
    private SectionBuilder parent;

    private List<BlockBuilder> blocks;

    public static SectionBuilder of(int level) {
        SectionBuilder builder = new SectionBuilder();
        builder.level = level;
        builder.blocks = new ArrayList<>();
        return builder;
    }

    public static SectionBuilder of(int level, SectionBuilder previous, SectionBuilder parent) {
        SectionBuilder builder = new SectionBuilder();
        builder.level = level;
        builder.previous = previous;
        builder.parent = parent;
        builder.blocks = new ArrayList<>();
        return builder;
    }

    public Section build() {
        List<Block> blocks = this.blocks
                                 .stream()
                                 .map(BlockBuilder::build)
                                 .collect(Collectors.toList());

        Section section = Section.of(level, Title.of(title), blocks);
        return section;
    }

    @Override
    public void addBlock(BlockBuilder builder) {
        this.blocks.add(builder);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getRef() {
//        return ref;
//    }

    public void setRef(String ref) {
        this.ref = ref;
    }

//    public AttributeList getAttList() {
//        return attList;
//    }

    public void setAttList(AttributeList attList) {
        this.attList = attList;
    }

    public SectionBuilder getNext() {
        return next;
    }

    public void setNext(SectionBuilder next) {
        this.next = next;
    }

//    public SectionBuilder getPrevious() {
//        return previous;
//    }

    public void setPrevious(SectionBuilder previous) {
        this.previous = previous;
    }

    public SectionBuilder getParent() {
        return parent;
    }

    public void setParent(SectionBuilder parent) {
        this.parent = parent;
    }
}
