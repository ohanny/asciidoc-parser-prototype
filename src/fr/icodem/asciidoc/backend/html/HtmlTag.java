package fr.icodem.asciidoc.backend.html;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum HtmlTag {
    DOCTYPE,
    HTML, BODY, HEAD,
    META, TITLE,
    DIV, SECTION, P, UL, OL, LI,
    H1, H2, H3, H4, H5, H6,
    BR,
    A, SPAN;

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

    private String buildAttributes(String... attributes) {
        String atts = null;
        if (attributes != null) {
            atts = IntStream.range(0, attributes.length)
                    .filter(i -> i % 2 == 0)
                    .mapToObj(i -> {
                        String str = attributes[i];
                        if (i + 1 < attributes.length && attributes[i + 1] != null) {
                            str += "=\"" + attributes[i + 1] + "\"";
                        }
                        return str;
                    })
                    .collect(Collectors.joining(" "));
        }
        if (atts.isEmpty()) atts = null;
        return atts;
    }

    private String buildStartTag(String name, String... attributes) {
        StringBuilder tag = new StringBuilder();
        tag.append("<").append(name);
        String atts = buildAttributes(attributes);
        if (atts != null) {
            tag.append(" ").append(atts);
        }
        tag.append(">");

        return tag.toString();
    }

    public String tag(String... attributes) {

        switch (this) {
            case DOCTYPE: return "<!DOCTYPE html>";
            case META: {
                return buildStartTag("meta", attributes);
            }
            case BR: return "<br>";
            default: return null;
        }
    }

    public String start(String... attributes) {
        switch (this) {
            case HTML: return "<html>";
            case BODY: return buildStartTag("body", attributes);
            case HEAD: return "<head>";
            case TITLE: return "<title>";
            case DIV: return buildStartTag("div", attributes);
            case SECTION: return "<section>";
            case P: return "<p>";
            case UL: return buildStartTag("ul", attributes);
            case OL: return buildStartTag("ol", attributes);
            case LI: return buildStartTag("li", attributes);
            case H1: return "<h1>";
            case H2: return "<h2>";
            case H3: return "<h3>";
            case H4: return "<h4>";
            case H5: return "<h5>";
            case H6: return "<h6>";
            case A: return buildStartTag("a", attributes);
            case SPAN: return buildStartTag("span", attributes);
            default: return null;
        }
    }

    public String end() {
        switch (this) {
            case HTML: return "</html>";
            case BODY: return "</body>";
            case HEAD: return "</head>";
            case TITLE: return "</title>";
            case DIV: return "</div>";
            case SECTION: return "</section>";
            case P: return "</p>";
            case UL: return "</ul>";
            case OL: return "</ol>";
            case LI: return "</li>";
            case H1: return "</h1>";
            case H2: return "</h2>";
            case H3: return "</h3>";
            case H4: return "</h4>";
            case H5: return "</h5>";
            case H6: return "</h6>";
            case SPAN: return "</span>";
            case A: return "</a>";
            default: return null;
        }
    }

}
