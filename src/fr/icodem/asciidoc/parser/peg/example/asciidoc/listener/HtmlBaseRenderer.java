package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.AttributeDefaults;
import fr.icodem.asciidoc.parser.elements.AttributeEntry;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.BlockRules;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.*;
import java.util.function.Consumer;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;

public abstract class HtmlBaseRenderer implements AsciidocRenderer, AsciidocHandler {
    private final static String NL = "\r\n";
    private final static String INDENT = "  ";

    private Writer writer;

    private BlockRules rules = new BlockRules(); // TODO inject

    protected Map<String, AttributeEntry> attributes;

    private StringBuilder buffer;
    private Consumer<String> bufferAppender;
    private Consumer<String> writerAppender;
    private Consumer<String> appender;

    private int positionInBuffer;
    private Marker marker; // current marker in use

    private class Marker {
        int position;
        Indenter indenter;

        public Marker(int position, int indentLevel) {
            this.position = position;
            this.indenter = new Indenter(indentLevel);
        }
    }

    private Map<String, Marker> markers;

    private class Indenter {
        int level;

        public Indenter() {}

        public Indenter(int level) {
            this.level = level;
        }

        void increment() {
            level++;
        }

        void decrement() {
            level--;
        }
    }
    private Indenter rootIndenter;
    //private Indenter markerIndenter; // when buffer is used
    private Indenter indenter;


    protected HtmlBaseRenderer(Writer writer) {
        this.writer = writer;

        List<AttributeEntry> attributes = new ArrayList<>();

        this.attributes = AttributeDefaults.Instance.getAttributes();
        for (AttributeEntry att : attributes) {
            this.attributes.put(att.getName(), att);
        }

        rules.useFactory(defaultRulesFactory());

        buffer = new StringBuilder();

        writerAppender = this::appendToWriter;
        bufferAppender = this::appendToBuffer;
        appender = writerAppender;
        markers = new HashMap<>();
        positionInBuffer = -1;

        rootIndenter = new Indenter();
        indenter = rootIndenter;
    }

    @Override
    public void render(String text) {
        ParsingResult result = new ParseRunner(rules, rules::document)
                //.trace()
                .parse(new StringReader(text), new AsciidocListener(this), null, null);

        closeWriter();
    }

    protected String getAttributeValue(String key) {
        return attributes.get(key).getValue();
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

    private void appendToWriter(String str) {
        try {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendToBuffer(String str) {
        if (positionInBuffer == -1) {
            buffer.append(str);
        } else {
            buffer.insert(positionInBuffer, str);
            positionInBuffer += str.length();
        }
    }

    protected HtmlBaseRenderer append(String str) {
        appender.accept(str);
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
        return indent(indenter.level);
    }

    private HtmlBaseRenderer indent(int times) {
        for (int i = 0; i < times; i++) {
            append(INDENT);
        }
        return this;
    }

    protected HtmlBaseRenderer incrementIndentLevel() {// TODO rename method (shorter name) ?
        indenter.increment();
        return this;
    }

    protected HtmlBaseRenderer decrementIndentLevel() {// TODO rename method (shorter name) ?
        indenter.decrement();
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

    public HtmlBaseRenderer mark(String key) {
        markers.put(key, new Marker(buffer.length(), indenter.level));
        return this;
    }

    protected HtmlBaseRenderer bufferOn() {
        appender = bufferAppender;
        return this;
    }

    protected HtmlBaseRenderer bufferOff() {
        if (buffer.length() > 0) {
            appendToWriter(buffer.toString());
            buffer.setLength(0);
        }
        appender = writerAppender;
        positionInBuffer = -1;
        indenter = rootIndenter;
        markers.clear();
        return this;
    }

    protected HtmlBaseRenderer moveTo(String key) {
        marker = markers.get(key);
        if (marker != null) {
            positionInBuffer = marker.position;
            indenter = marker.indenter;
        } else {
            throw new IllegalStateException("No marker found for key : " + key);
        }
        return this;
    }

    protected HtmlBaseRenderer moveLast() {
        int pos = marker.position; // position when moveTo() was called
        int delta = positionInBuffer - marker.position; // number of chars added after moveTo()

        markers.values()
               .stream()
               .filter(m -> m.position >= pos)
               .forEach(m -> m.position += delta);

        positionInBuffer = -1;
        indenter = rootIndenter;

        return this;
    }

}
