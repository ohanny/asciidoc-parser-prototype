package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Content;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Preamble;

public class ContentBuilder implements BlockBuilder {

    private BuildState state;

    private PreambleBuilder preambleBuilder;
    private SectionBuilder firstSection;
    private SectionBuilder currentSection;
    private SectionListBuilder sectionListBuilder;

    public static ContentBuilder newBuilder(BuildState state) {
        ContentBuilder builder = new ContentBuilder();
        builder.state = state;
        builder.sectionListBuilder = SectionListBuilder.newBuilder();

        return builder;
    }

    @Override
    public Content build() {
        Preamble preamble = (preambleBuilder == null) ? null : preambleBuilder.build();

        return Content.of(preamble, sectionListBuilder.build(firstSection));
    }

    public void addPreamble() {
        preambleBuilder = PreambleBuilder.newBuilder();
        state.pushBlockContainer(preambleBuilder);
    }

    public void closePreamble() {
        state.popBlockContainer();
    }

    public void newSection(int level, AttributeList attList) {
        if (firstSection != null) {
            // close parents and get new section parent
            SectionBuilder parent = checkExitSection(level);

            SectionBuilder previous = currentSection;
            currentSection = SectionBuilder.of(attList, level, previous, parent);
            previous.setNext(currentSection);
        } else {
            firstSection = SectionBuilder.of(attList, level);
            currentSection = firstSection;
        }

        //currentSection.setAttList(attList);
        state.pushBlockContainer(currentSection);
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
        state.popBlockContainer();
    }

    public void setSectionTitle(String title) {
        currentSection.setTitle(title);
        currentSection.setRef(state.textToRef(title));
    }

    public void closeContent() {
        checkExitSection(-1);
    }

}
