package fr.icodem.asciidoc.backend.html;

import fr.icodem.asciidoc.parser.AsciidocParserBaseHandler;

import java.io.IOException;
import java.io.Writer;

public class HtmlBaseBackend extends AsciidocParserBaseHandler {
    private final static String NL = "\r\n";
    private final static String INDENT = "  ";

    protected Writer writer;
    private int indentLevel = 0;

    public HtmlBaseBackend(Writer writer) {
        this.writer = writer;
    }

    protected void closeWriter() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected HtmlBaseBackend append(String str) {
        try {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    protected HtmlBaseBackend nl() {
        append(NL);
        return this;
    }

    protected HtmlBaseBackend indent() {
        return indent(indentLevel);
    }

    private HtmlBaseBackend indent(int times) {
        for (int i = 0; i < times; i++) {
            append(INDENT);
        }
        return this;
    }

    protected HtmlBaseBackend incrementIndentLevel() {
        indentLevel++;
        return this;
    }

    protected HtmlBaseBackend decrementIndentLevel() {
        indentLevel--;
        return this;
    }
}
