package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.AttributeDefaults;
import fr.icodem.asciidoc.parser.elements.AttributeEntry;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.BlockRules;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.io.Writer;
import java.util.*;
import java.util.function.Consumer;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;

public abstract class HtmlBaseRenderer implements AsciidocRenderer, AsciidocHandler {

    private BlockRules rules = new BlockRules(); // TODO inject

    protected Map<String, AttributeEntry> attributes;

    private TextOutputter outputter;

    protected HtmlBaseRenderer(Writer writer) {
        this.outputter = new TextOutputter(writer);

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
                .parse(new StringReader(text), new BlockListener(this), null, null);

        outputter.closeWriter();
        //closeWriter();
    }

    protected String getAttributeValue(String key) {
        return attributes.get(key).getValue();
    }

    protected HtmlBaseRenderer append(String str) {
        outputter.append(str);
        return this;
    }

    protected HtmlBaseRenderer include(Runnable r) {
        r.run();
        return this;
    }

    protected HtmlBaseRenderer nl() {
        outputter.nl();
        return this;
    }

    protected HtmlBaseRenderer indent() {
        outputter.indent();
        return this;
    }

    protected HtmlBaseRenderer incrementIndentLevel() {// TODO rename method (shorter name) ?
        outputter.incrementIndentLevel();
        return this;
    }

    protected HtmlBaseRenderer decrementIndentLevel() {// TODO rename method (shorter name) ?
        outputter.decrementIndentLevel();
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

    protected HtmlBaseRenderer mark(String key) {
        outputter.mark(key);
        return this;
    }

    protected HtmlBaseRenderer bufferOn() {
        outputter.bufferOn();
        return this;
    }

    protected HtmlBaseRenderer bufferOff() {
        outputter.bufferOff();
        return this;
    }

    protected HtmlBaseRenderer moveTo(String key) {
        outputter.moveTo(key);
        return this;
    }

    protected HtmlBaseRenderer moveEnd() {
        outputter.moveEnd();
        return this;
    }

}
