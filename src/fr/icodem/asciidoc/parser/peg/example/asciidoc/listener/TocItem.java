package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import java.util.LinkedList;
import java.util.List;

public class TocItem {
    private int level;
    private String title;
    private String ref;

    private List<TocItem> children;

    private TocItem(int level, String title, String ref) {
        this.level = level;
        this.title = title;
        this.ref = ref;
        this.children = new LinkedList<>();
    }

    public static TocItem of(int level, String title, String ref) {
        return new TocItem(level, title, ref);
    }

    public int getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public String getRef() {
        return ref;
    }

    public List<TocItem> getChildren() {
        return children;
    }
}
