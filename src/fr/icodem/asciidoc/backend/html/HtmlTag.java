package fr.icodem.asciidoc.backend.html;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum HtmlTag {
    DOCTYPE,
    HTML, BODY, HEAD,
    META, TITLE, LINK, SCRIPT, STYLE,
    DIV, SECTION, HEADER,
    P, BLOCKQUOTE, CITE, UL, OL, LI, DL, DT, DD,
    TABLE, COLGROUP, COL, THEAD, TH, TBODY, TFOOT, TR, TD,
    PRE,
    H1, H2, H3, H4, H5, H6,
    BR, HR,
    A, IMG, SPAN, STRONG, EM, SUB, SUP, CODE, MARK, I, B;

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
                        } else {
                            str = null;
                        }
                        return str;
                    })
                    .filter(str -> str != null)
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
            case LINK: {
                return buildStartTag("link", attributes);
            }
            case BR: return "<br>";
            case HR: return "<hr>";
            case COL: {
                return buildStartTag("col", attributes);
            }
            case IMG: {
                return buildStartTag("img", attributes);
            }
            default: return null;
        }
    }

    public String start(String... attributes) {
        switch (this) {
            case HTML: return "<html>";
            case BODY: return buildStartTag("body", attributes);
            case HEAD: return "<head>";
            case TITLE: return "<title>";
            case SCRIPT: return buildStartTag("script", attributes);
            case STYLE: return "<style>";
            case DIV: return buildStartTag("div", attributes);
            case SECTION: return buildStartTag("section", attributes);
            case HEADER: return buildStartTag("header", attributes);
            case P: return buildStartTag("p", attributes);
            case BLOCKQUOTE: return "<blockquote>";
            case CITE: return "<cite>";
            case UL: return buildStartTag("ul", attributes);
            case OL: return buildStartTag("ol", attributes);
            case LI: return buildStartTag("li", attributes);
            case DL: return "<dl>";
            case DT: return buildStartTag("dt", attributes);
            case DD: return "<dd>";
            case TABLE: return buildStartTag("table", attributes);
            case COLGROUP: return "<colgroup>";
            case TBODY: return "<tbody>";
            case THEAD: return "<thead>";
            case TH: return buildStartTag("th", attributes);
            case TFOOT: return "<tfoot>";
            case TR: return "<tr>";
            case TD: return buildStartTag("td", attributes);
            case PRE: return buildStartTag("pre", attributes);
            case H1: return buildStartTag("h1", attributes);
            case H2: return buildStartTag("h2", attributes);
            case H3: return buildStartTag("h3", attributes);
            case H4: return buildStartTag("h4", attributes);
            case H5: return buildStartTag("h5", attributes);
            case H6: return buildStartTag("h6", attributes);
            case A: return buildStartTag("a", attributes);
            case SPAN: return buildStartTag("span", attributes);
            case STRONG: return "<strong>";
            case EM: return "<em>";
            case SUB: return "<sub>";
            case SUP: return "<sup>";
            case CODE: return buildStartTag("code", attributes);
            case MARK: return buildStartTag("mark", attributes);
            case I: return buildStartTag("i", attributes);
            case B: return buildStartTag("b", attributes);
            default: return null;
        }
    }

    public String end() {
        switch (this) {
            case HTML: return "</html>";
            case BODY: return "</body>";
            case HEAD: return "</head>";
            case TITLE: return "</title>";
            case SCRIPT: return "</script>";
            case STYLE: return "</style>";
            case DIV: return "</div>";
            case SECTION: return "</section>";
            case HEADER: return "</header>";
            case P: return "</p>";
            case BLOCKQUOTE: return "</blockquote>";
            case CITE: return "</cite>";
            case UL: return "</ul>";
            case OL: return "</ol>";
            case LI: return "</li>";
            case DL: return "</dl>";
            case DT: return "</dt>";
            case DD: return "</dd>";
            case TABLE: return "</table>";
            case COLGROUP: return "</colgroup>";
            case TBODY: return "</tbody>";
            case THEAD: return "</thead>";
            case TH: return "</th>";
            case TFOOT: return "</tfoot>";
            case TR: return "</tr>";
            case TD: return "</td>";
            case PRE: return "</pre>";
            case H1: return "</h1>";
            case H2: return "</h2>";
            case H3: return "</h3>";
            case H4: return "</h4>";
            case H5: return "</h5>";
            case H6: return "</h6>";
            case SPAN: return "</span>";
            case A: return "</a>";
            case STRONG: return "</strong>";
            case EM: return "</em>";
            case SUB: return "</sub>";
            case SUP: return "</sup>";
            case CODE: return "</code>";
            case MARK: return "</mark>";
            case I: return "</i>";
            case B: return "</b>";
            default: return null;
        }
    }

}
