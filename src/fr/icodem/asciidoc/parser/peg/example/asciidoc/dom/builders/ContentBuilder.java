package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Content;

import java.util.HashMap;
import java.util.Map;

public class ContentBuilder implements BlockBuilder {

    private SectionBuilder firstSection;
    private SectionBuilder currentSection;
    private SectionListBuilder sectionListBuilder;
    private BuildState state;

    public static ContentBuilder newBuilder(BuildState state) {
        ContentBuilder builder = new ContentBuilder();
        builder.state = state;
        builder.sectionListBuilder = SectionListBuilder.newBuilder();

        return builder;
    }

    @Override
    public Content build() {
        // TODO check null ?
        return sectionListBuilder == null ? null : Content.of(sectionListBuilder.build(firstSection));
    }

    public void newSection(int level, AttributeList attList) {
        if (firstSection != null) {
            // close parents and get new section parent
            SectionBuilder parent = checkExitSection(level);

            SectionBuilder previous = currentSection;
            currentSection = SectionBuilder.of(level, previous, parent);
            previous.setNext(currentSection);
        } else {
            firstSection = SectionBuilder.of(level);
            currentSection = firstSection;
        }

        currentSection.setAttList(attList);
        state.pushContainer(currentSection);
    }

    /**
     *
     * @param newSectionLevel new section level; -1 if not a new section, but the end of document
     * @return the parent level
     */
    private SectionBuilder checkExitSection(int newSectionLevel) {
        if (currentSection == null) return null;

        SectionBuilder parent = null;
        if (newSectionLevel == currentSection.getLevel()) {
            //handler.endSection(currentSection.getLevel());
            closeSection();
            parent = currentSection.getParent();
        } else if (newSectionLevel < currentSection.getLevel()) {
            //handler.endSection(currentSection.getLevel());
            closeSection();
            SectionBuilder p = currentSection.getParent();
            while (p != null) {
                if (p.getLevel() > newSectionLevel) {
                    closeSection();
                    //handler.endSection(p.getLevel());
                    p = p.getParent();
                } else if (p.getLevel() == newSectionLevel) {
                    closeSection();
                    //handler.endSection(p.getLevel());
                    parent = p.getParent();
                    break;
                } else {
                    parent = p;
                    break;
                }
            }
        } else {
            parent = currentSection;
        }

        return parent;
    }

    private void closeSection() {
        state.popContainer();
    }

    public void setSectionTitle(String title) {
        currentSection.setTitle(title);
        currentSection.setRef(textToRef(title));
    }

    // TODO duplicate
    // computed refs : helps avoid duplicates
    private Map<String, Integer> refs = new HashMap<>();
    private String textToRef(String text) {
        String ref = text.toLowerCase().replaceAll("\\s+", "_");
        int count = refs.getOrDefault(ref, 0);
        refs.put(ref, ++count);
        if (count > 1) {
            ref = ref + "_" + count;
        }
        return ref;
    }

    // TODO rename ?
    public void exitDocument() {
        checkExitSection(-1);
    }

}
