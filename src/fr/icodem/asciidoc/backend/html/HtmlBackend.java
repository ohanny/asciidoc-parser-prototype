package fr.icodem.asciidoc.backend.html;

import fr.icodem.asciidoc.parser.elements.*;

import java.io.Writer;

import static fr.icodem.asciidoc.backend.html.HtmlTag.*;

public class HtmlBackend extends HtmlBaseBackend {

    public HtmlBackend(Writer writer) {
        super(writer);
    }

    @Override
    public void startDocument(Document doc) {
        append(DOCTYPE.tag()).nl()
                .append(HTML.start()).nl()
                .append(HEAD.start()).nl().incrementIndentLevel()
                .indent().append(META_CHARSET.tag()).nl()
                .append(HEAD.end()).nl().decrementIndentLevel()
                .append(BODY.start()).nl().incrementIndentLevel();

        /*
        class Feature {
            String description;

            public Feature(String description) {
                this.description = description;
            }
        }

        try {
            Map<String, Object> scopes = new HashMap<>();
            scopes.put("name", "Mustache");
            scopes.put("feature", new Feature("Perfect !"));

            Writer writer = new OutputStreamWriter(System.out);
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(new StringReader("{{name}}, {{feature.description}}"), "example");
            mustache.execute(writer, scopes);
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
        */

    }

    @Override
    public void endDocument(Document doc) {
        append(BODY.end())
                .nl().decrementIndentLevel()
                .append(HTML.end());

        closeWriter();
    }

    @Override
    public void startDocumentTitle(DocumentTitle docTitle) {
        indent().append(H1.start());
    }

    @Override
    public void endDocumentTitle(DocumentTitle docTitle) {
        append(H1.end()).nl();
    }

    @Override
    public void startTitle(Title title) {
        append(title.getText());
    }

    @Override
    public void startParagraph(Paragraph p) {
        indent().append(P.start())
                .append(p.getText())
                .append(P.end())
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
        indent().append(getTitleHeader(sectionTitle.getLevel()).start());
    }

    @Override
    public void endSectionTitle(SectionTitle sectionTitle) {
        append(getTitleHeader(sectionTitle.getLevel()).end()).nl();
    }

}
