package fr.icodem.asciidoc.parser.peg.example.asciidoc;

public interface UrlUtils {
    static boolean isUrl(String xref) {
        return xref.startsWith("https://") || xref.startsWith("http://")
                || xref.startsWith("file://");
    }
}
