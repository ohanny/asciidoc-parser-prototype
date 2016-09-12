package fr.icodem.asciidoc.parser.backend

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements

class FormattedTextSpec extends BackendBaseSpecification {

    def "two bold words within phrase"() {
        given:
        String input = "A *very nice* day";

        when:
        Document doc = transform(input);

        then:
        Element paragraph = doc.select("div[class=paragraph] > p").first();
        paragraph != null;
        paragraph.text() == "A very nice day";

        Element bold = paragraph.select("strong").first();
        bold != null;
        bold.text() == "very nice";
    }

    def "nested italic word within bold words"() {
        given:
        String input = "A *very _sunny_ nice* day";

        when:
        Document doc = transform(input);

        then:
        Element paragraph = doc.select("div[class=paragraph] > p").first();
        paragraph != null;
        paragraph.text() == "A very sunny nice day";

        Element bold = paragraph.select("strong").first();
        bold != null;
        bold.text() == "very sunny nice";

        Element italic = bold.select("em").first();
        italic != null;
        italic.text() == "sunny";

    }
}