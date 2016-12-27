package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import java.util.LinkedList;
import java.util.List;

public class TocItem {
    private int level;
    private String title;

    private List<TocItem> children;

    private TocItem(int level, String title) {
        this.level = level;
        this.title = title;
        this.children = new LinkedList<>();
    }

    public static TocItem of(int level, String title) {
        return new TocItem(level, title);
    }

    public int getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public List<TocItem> getChildren() {
        return children;
    }
}
