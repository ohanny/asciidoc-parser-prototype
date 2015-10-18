package fr.icodem.asciidoc.backend.html;

public enum HtmlTag {
    DOCTYPE, META_CHARSET,
    HTML, BODY, HEAD,
    SECTION, P,
    H1, H2, H3, H4, H5, H6;

    public static HtmlTag getTitleHeader(int level) {
        switch (level) {
            case 1: return H1;
            case 2: return H2;
            case 3: return H3;
            case 4: return H4;
            case 5: return H5;
            case 6: return H6;
            default: return H1;
        }
    }

    public String tag() {
        switch (this) {
            case DOCTYPE: return "<!DOCTYPE html>";
            case META_CHARSET: return "<meta charset=\"UTF-8\">";
            default: return null;
        }
    }

    public String start() {
        switch (this) {
            case HTML: return "<html>";
            case BODY: return "<body>";
            case HEAD: return "<head>";
            case SECTION: return "<section>";
            case P: return "<p>";
            case H1: return "<h1>";
            case H2: return "<h2>";
            case H3: return "<h3>";
            case H4: return "<h4>";
            case H5: return "<h5>";
            case H6: return "<h6>";
            default: return null;
        }
    }

    public String end() {
        switch (this) {
            case HTML: return "</html>";
            case BODY: return "</body>";
            case HEAD: return "</head>";
            case SECTION: return "</section>";
            case P: return "</p>";
            case H1: return "</h1>";
            case H2: return "</h2>";
            case H3: return "</h3>";
            case H4: return "</h4>";
            case H5: return "</h5>";
            case H6: return "</h6>";
            default: return null;
        }
    }

}
