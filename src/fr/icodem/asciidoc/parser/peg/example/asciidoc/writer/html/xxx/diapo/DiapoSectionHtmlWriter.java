package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Section;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.SectionHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.SECTION;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.getTitleHeader;

public class DiapoSectionHtmlWriter extends SectionHtmlWriter<DiapoSectionHtmlWriter> {

    public DiapoSectionHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void start(Section section) {
        String classes = getMoreClasses("slide", section.getAttributes());
        String position = Integer.toString(section.getPosition() + 1);
        indent()
          .append(SECTION.start("class", classes, "data-slide-index", position)).nl()
          .incIndent()
            .includeSectionTitle(section)
        ;

    }

    public DiapoSectionHtmlWriter includeSectionTitle(Section section) {
        String ref = "";

        if (section.getAttributes() != null && section.getAttributes().hasOption("conceal")) return this;

        HtmlTag titleHeader = getTitleHeader(2);
        return indent().append(titleHeader.start("id", ref))
                .append(section.getTitle().getText()).append(titleHeader.end()).nl();
    }

    @Override
    protected void end(Section section) {
        decIndent()
          .indent().append(SECTION.end()).nl();
    }
}
