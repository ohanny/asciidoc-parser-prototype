package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.AttributeDefaults;
import fr.icodem.asciidoc.parser.elements.AttributeEntry;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.BlockRules;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;

public abstract class HtmlBaseRenderer implements AsciidocRenderer, AsciidocHandler {
    private final static String NL = "\r\n";
    private final static String INDENT = "  ";

    private Writer writer;
    private int indentLevel = 0;

    private BlockRules rules = new BlockRules();

    protected Map<String, AttributeEntry> attributes;

    protected HtmlBaseRenderer(Writer writer) {
        this.writer = writer;

        List<AttributeEntry> attributes = new ArrayList<>();

        this.attributes = AttributeDefaults.Instance.getAttributes();
        for (AttributeEntry att : attributes) {
            this.attributes.put(att.getName(), att);
        }

        rules.useFactory(defaultRulesFactory());

    }

    @Override
    public void render(String text) {
        ParsingResult result = new ParseRunner(rules, rules::document)
                //.trace()
                .parse(new StringReader(text), new AsciidocListener(this), null, null);

        closeWriter();
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

    protected HtmlBaseRenderer append(String str) {
        try {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    protected HtmlBaseRenderer include(Runnable r) {
        r.run();
        return this;
    }

    protected HtmlBaseRenderer nl() {
        append(NL);
        return this;
    }

    protected HtmlBaseRenderer indent() {
        return indent(indentLevel);
    }

    private HtmlBaseRenderer indent(int times) {
        for (int i = 0; i < times; i++) {
            append(INDENT);
        }
        return this;
    }

    protected HtmlBaseRenderer incrementIndentLevel() {
        indentLevel++;
        return this;
    }

    protected HtmlBaseRenderer decrementIndentLevel() {
        indentLevel--;
        return this;
    }

    protected HtmlBaseRenderer runIf(boolean condition, Runnable runnable) {
        if (condition) runnable.run();
        return this;
    }

    protected <T> HtmlBaseRenderer forEach(List<T> elements, Consumer<T> c) {
        elements.forEach(c);
        return this;
    }

}
