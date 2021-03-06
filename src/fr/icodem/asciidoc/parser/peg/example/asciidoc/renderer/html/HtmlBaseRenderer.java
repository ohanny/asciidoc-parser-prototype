package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.BlockRules;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.AsciidocRenderer;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.DocumentWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.TextOutputter;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;


// TODO s'inspirer de JHtml
public abstract class HtmlBaseRenderer<HR extends HtmlBaseRenderer<HR>> implements AsciidocRenderer, AsciidocHandler {

    private BlockRules rules; // TODO inject

    private AttributeEntries attributeEntries;

    private TextOutputter outputter;

    private StyleAttributeBuilder styleAttributeBuilder;

    protected HtmlBaseRenderer(DocumentWriter writer) {
        attributeEntries = AttributeEntries.newAttributeEntries();
        outputter = new TextOutputter(writer);
        rules = new BlockRules(attributeEntries);
        rules.withFactory(defaultRulesFactory());
        styleAttributeBuilder = StyleAttributeBuilder.newIntance();
    }

    public HR withSourceResolver(SourceResolver resolver) {
        rules.withSourceResolver(resolver);
        return (HR)this;
    }

    @Override
    public void render(File source) {
        try {
            String text =
                    Files.readAllLines(source.toPath())
                            .stream()
                            .collect(Collectors.joining("\n"));
            attributeEntries.addAttribute(AttributeEntry.of("docdir", source.getParent()));
            render(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(String text) {
        final BlockListener listener = new BlockListener(this, attributeEntries);

        ParsingResult result = new ParseRunner(rules, rules::document)
                //.trace()
                .parse(new StringReader(text), listener, null, null);

        listener.postProcess();

        outputter.closeWriter();
    }

//    @Override
//    public void attributeEntries(AttributeEntries atts) { // TODO is this method useful in renderer ?
//        this.attributeEntries = atts;
//    }

    protected AttributeEntry getAttributeEntry(String name) {
        return attributeEntries.getAttribute(name);
    }

    protected String getAttributeEntryValue(String name) {
        return attributeEntries.getAttribute(name).getValue();
    }

    public boolean isAttributeValueEqualTo(String name, String value) {
        return attributeEntries.isAttributeValueEqualTo(name, value);
    }

    public boolean isAttributeEnabled(String name) {
        return attributeEntries.isAttributeEnabled(name);
    }

    protected StyleAttributeBuilder styleBuilder() {
        return styleAttributeBuilder;
    }

    protected HR append(String str) {
        outputter.append(str);
        return (HR)this;
    }

    protected HR include(Runnable r) {
        r.run();
        return (HR)this;
    }

    protected HR nl() {
        outputter.nl();
        return (HR)this;
    }

    protected HR indent() {
        outputter.indent();
        return (HR)this;
    }

    protected HR incIndent() {// TODO rename method (shorter name) ?
        outputter.incIndent();
        return (HR)this;
    }

    protected HR decIndent() {// TODO rename method (shorter name) ?
        outputter.decIndent();
        return (HR)this;
    }

    protected HR append(Runnable runnable) {
        runnable.run();
        return (HR)this;
    }

    protected HR appendIf(boolean condition, Runnable runnable) {
        if (condition) runnable.run();
        return (HR)this;
    }

    protected <T> HR forEach(List<T> elements, Consumer<T> c) {
        elements.forEach(c);
        return (HR)this;
    }

    protected <T> HR forEach(List<T> elements, BiConsumer<T, Integer> c) {
        for (int i = 0; i < elements.size(); i++) {
            c.accept(elements.get(i), i);
        }
        return (HR)this;
    }

    protected <T> HR forEach(List<T> elements, Predicate<T> p, Consumer<T> c) {
        elements.stream()
                .filter(p)
                .forEach(c);
        return (HR)this;
    }

    // mark for first pass
    protected HR mark(String key) {
        outputter.mark(key);
        return (HR)this;
    }

    protected HR bufferOn() {
        outputter.bufferOn();
        return (HR)this;
    }

    protected HR bufferOff() {
        outputter.bufferOff();
        return (HR)this;
    }

    protected HR moveTo(String key) {
        outputter.moveTo(key);
        return (HR)this;
    }

    protected HR moveEnd() {
        outputter.moveEnd();
        return (HR)this;
    }

    // mark for post-processing
    protected HR markOnWriter(String key) {
        outputter.markOnWriter(key);
        return (HR)this;
    }

    protected HR seekOnWriter(String key) {
        outputter.seekOnWriter(key);
        return (HR)this;
    }

    protected HR endInsertOnWriter() {
        outputter.endInsertOnWriter();
        return (HR)this;
    }

    protected HR trace(String message) {
        System.out.println(message);
        return (HR)this;
    }
}
