package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.BlockRules;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AsciidocHandler;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.BlockListener;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.SourceResolver;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.AsciidocRenderer;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.DocumentWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.TextOutputter;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.util.List;
import java.util.function.Consumer;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;

public abstract class HtmlBaseRenderer implements AsciidocRenderer, AsciidocHandler {

    private BlockRules rules = new BlockRules(); // TODO inject

    private AttributeEntries attributeEntries;

    private TextOutputter outputter;

    protected HtmlBaseRenderer(DocumentWriter writer) {
        outputter = new TextOutputter(writer);
        rules.withFactory(defaultRulesFactory());
    }

    public HtmlBaseRenderer withSourceResolver(SourceResolver resolver) {
        rules.withSourceResolver(resolver);
        return this;
    }

    @Override
    public void render(String text) {
        final BlockListener listener = new BlockListener(this);

        ParsingResult result = new ParseRunner(rules, rules::document)
                //.trace()
                .parse(new StringReader(text), listener, null, null);

        listener.postProcess();

        outputter.closeWriter();
    }

    @Override
    public void attributeEntries(AttributeEntries atts) { // TODO is this method useful in renderer ?
        this.attributeEntries = atts;
    }

    protected fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeEntry getAttributeEntry(String name) {
        return attributeEntries.getAttribute(name);
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

    protected HtmlBaseRenderer incIndent() {// TODO rename method (shorter name) ?
        outputter.incIndent();
        return this;
    }

    protected HtmlBaseRenderer decIndent() {// TODO rename method (shorter name) ?
        outputter.decIndent();
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

    // mark for first pass
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

    // mark for post-processing
    protected HtmlBaseRenderer markOnWriter(String key) {
        outputter.markOnWriter(key);
        return this;
    }

    protected HtmlBaseRenderer seekOnWriter(String key) {
        outputter.seekOnWriter(key);
        return this;
    }

    protected HtmlBaseRenderer endInsertOnWriter() {
        outputter.endInsertOnWriter();
        return this;
    }
}
