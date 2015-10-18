package fr.icodem.asciidoc.backend.html;

import fr.icodem.asciidoc.parser.AsciidocParserBaseHandler;
import fr.icodem.asciidoc.parser.elements.*;

import java.io.IOException;
import java.io.Writer;

import static fr.icodem.asciidoc.parser.HtmlTag.H1;
import static fr.icodem.asciidoc.parser.HtmlTag.getTitleHeader;

public class HtmlBackend extends AsciidocParserBaseHandler {

    private final static String NL = "\r\n";
    private final static String INDENT = "  ";

    private int indentLevel = 0;

    private Writer writer;

    public HtmlBackend(Writer writer) {
        this.writer = writer;
    }

    private void closeWriter() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HtmlBackend append(String str) {
        try {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    private HtmlBackend nl() {
        append(NL);
        return this;
    }

    private HtmlBackend indent() {
        return indent(indentLevel);
    }

    private HtmlBackend indent(int times) {
        for (int i = 0; i < times; i++) {
            append(INDENT);
        }
        return this;
    }

    private HtmlBackend incrementIndentLevel() {
        indentLevel++;
        return this;
    }

    private HtmlBackend decrementIndentLevel() {
        indentLevel--;
        return this;
    }

    @Override
    public void startDocument(Document doc) {
        append("<!DOCTYPE html>").nl()
                .append("<html>").nl()
                .append("<head>").nl().incrementIndentLevel()
                .indent().append("<meta charset=\"UTF-8\">").nl()
                .append("</head>").nl().decrementIndentLevel()
                .append("<body>").nl().incrementIndentLevel();

        //System.out.println(ctx.getChildCount());
        //System.out.println(ctx.getText());

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
        append("</body>").nl().decrementIndentLevel()
                .append("</html>");

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
        indent().append("<p>").append(p.getText()).append("</p>").nl();
    }

    @Override
    public void startSection(Section section) {
        indent().append("<section>").nl().incrementIndentLevel();
    }

    @Override
    public void endSection(Section section) {
        decrementIndentLevel().indent().append("</section>").nl();
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
