package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class Toc {
    private TocItem root;

    private Toc(TocItem root) {
        this.root = root;
    }

    public static Toc of(TocItem root) {
        Toc toc = new Toc(root);
        return toc;
    }

    public TocItem getRoot() {
        return root;
    }
}
