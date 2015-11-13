package fr.icodem.asciidoc.backend.html;

import fr.icodem.asciidoc.parser.elements.*;

import java.io.Writer;
import java.util.stream.Collectors;

import static fr.icodem.asciidoc.backend.html.HtmlTag.*;

public class HtmlBackendDelegate extends HtmlBaseBackend {

    // If document title is not present, the first detected section title is used.
    // If no section is present in document, then title is equal to "Untitled"
    String fallbackDocumentTitle = "Untitled";

    public HtmlBackendDelegate(Writer writer) {
        super(writer);
    }

    @Override
    public void startDocument(Document doc) {
        String title = (doc.getTitle() != null)?doc.getTitle().getText():fallbackDocumentTitle;

        append(DOCTYPE.tag()).nl()
                .append(HTML.start()).nl()
                .append(HEAD.start()).nl().incrementIndentLevel()
                .indent().append(META.tag("charset", "UTF-8")).nl()
                .indent().append(META.tag("name", "viewport", "content", "width=device-width, initial-scale=1.0")).nl()
                .indent().append(META.tag("name", "generator", "content", "xxx")).nl()
                .runIf(doc.getAuthors().size() > 0,
                        () -> indent().append(META.tag("name", "author", "content",
                                doc.getAuthors()
                                        .stream()
                                        .map(a -> a.getName())
                                        .collect(Collectors.joining(", "))))
                                .nl())
                .indent().append(TITLE.start()).append(title).append(TITLE.end()).nl()
                .append(HEAD.end()).nl().decrementIndentLevel()
                .append(BODY.start("class", doc.getAttributeValue("doctype"))).nl().incrementIndentLevel()
                .runIf(doc.isHeaderPresent(), () -> {
                    indent().append(DIV.start("id", "header")).nl().incrementIndentLevel()
                            .indent().append(H1.start()).append(doc.getTitle().getText()).append(H1.end()).nl()
                            .runIf(doc.isAuthorsPresent(), () -> {
                                indent().append(DIV.start("class", "details")).nl().incrementIndentLevel()
                                        .forEach(doc.getAuthors(), a -> {
                                            String index = "" + ((a.getPosition() == 1) ? "" : "" + a.getPosition());
                                            indent().append(SPAN.start("id", "author" + index, "class", "author"))
                                                    .append(a.getName()).append(SPAN.end()).append(BR.tag()).nl()
                                                    .runIf(a.getAddress() != null, () -> {
                                                        indent().append(SPAN.start("id", "email" + index, "class", "email"))
                                                                .append(A.start("href", "mailto:" + a.getAddress()))
                                                                .append(a.getAddress()).append(A.end())
                                                                .append(SPAN.end()).append(BR.tag()).nl();
                                                    });
                                        })
                                        .decrementIndentLevel().indent().append(DIV.end()).nl();
                            })
                            .decrementIndentLevel().indent().append(DIV.end()).nl();
                })
                .indent().append(DIV.start("id", "content")).incrementIndentLevel().nl();
    }


    @Override
    public void startPreamble() {
        indent().append(DIV.start("id", "preamble")).incrementIndentLevel().nl();
    }

    @Override
    public void endPreamble() {
        decrementIndentLevel().indent().append(DIV.end()).nl();
    }

    @Override
    public void endDocument(Document doc) {
        decrementIndentLevel().indent().append(DIV.end()).nl() // content end
            .append(BODY.end()).nl()
                    .decrementIndentLevel().append(HTML.end());

        closeWriter();
    }

    @Override
    public void startParagraph(Paragraph p) {
        indent().append(DIV.start("class", "paragraph")).nl()
                .incrementIndentLevel()
                .indent().append(P.start())
                .append(p.getText())
                .append(P.end()).nl()
                .decrementIndentLevel()
                .indent().append(DIV.end())
                .nl();
    }

    @Override
    public void startSection(Section section) {
        indent().append(SECTION.start()).nl().incrementIndentLevel();
    }

    @Override
    public void endSection(Section section) {
        decrementIndentLevel().indent().append(SECTION.end()).nl();
    }

    @Override
    public void startSectionTitle(SectionTitle sectionTitle) {
        indent().append(getTitleHeader(sectionTitle.getLevel()).start())
                .append(sectionTitle.getText())
                .append(getTitleHeader(sectionTitle.getLevel()).end()).nl();
    }

    @Override
    public void startAttributeEntry(AttributeEntry att) {
    }

}

