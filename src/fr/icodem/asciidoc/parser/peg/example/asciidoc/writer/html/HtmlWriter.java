package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class HtmlWriter<HW extends HtmlWriter<HW>> {

    private final static String NL = "\r\n";
    private final static String INDENT = "  ";
    
    private Outputter outputter;
    protected WriterState state; // TODO dependency to model writer : to be refactored

    public HtmlWriter(Outputter outputter, WriterState state) {
        this.outputter = outputter;
        this.state = state;
    }

    public HW append(String str) {
        outputter.print(str);
        return (HW)this;
    }

    public HW include(Runnable r) {
        r.run();
        return (HW)this;
    }

    public HW nl() {
        append(NL);
        return (HW)this;
    }

    public HW indent() {
        for (int i = 0; i < state.getIndent(); i++) {
            append(INDENT);
        }
        return (HW)this;
    }

    public HW incIndent() {
        state.incIndent();
        return (HW)this;
    }

    public HW decIndent() {
        state.decIndent();
        return (HW)this;
    }

    public HW append(Runnable runnable) {
        runnable.run();
        return (HW)this;
    }

    public HW appendIf(boolean condition, Runnable runnable) {
        if (condition) runnable.run();
        return (HW)this;
    }

    public <T> HW forEach(List<T> elements, Consumer<T> c) {
        elements.forEach(c);
        return (HW)this;
    }

    public <T> HW forEach(List<T> elements, BiConsumer<T, Integer> c) {
        for (int i = 0; i < elements.size(); i++) {
            c.accept(elements.get(i), i);
        }
        return (HW)this;
    }

    public <T> HW forEach(List<T> elements, Predicate<T> p, Consumer<T> c) {
        elements.stream()
                .filter(p)
                .forEach(c);
        return (HW)this;
    }

    public HW trace(String message) {
        System.out.println(message);
        return (HW)this;
    }

    protected StyleAttributeBuilder styleBuilder() {
        return state.getStyleAttributeBuilder();
    }

}
