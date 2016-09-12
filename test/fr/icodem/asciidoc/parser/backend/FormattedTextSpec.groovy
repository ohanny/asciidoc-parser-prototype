package fr.icodem.asciidoc.parser.backend

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements

class FormattedTextSpec extends BackendBaseSpecification {

    def "bold phrase"() {
        given:
        String input = "*A very nice day*";

        when:
        Document doc = transform(input);

        then:
        Element paragraph = doc.select("div[class=paragraph] > p").first();
        paragraph != null;
        paragraph.text() == "A very nice day";

        Element bold = paragraph.select("strong").first();
        bold != null;
        bold.text() == "A very nice day";
    }

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

    def "italic phrase"() {
        given:
        String input = "_A very nice day_";

        when:
        Document doc = transform(input);

        then:
        Element paragraph = doc.select("div[class=paragraph] > p").first();
        paragraph != null;
        paragraph.text() == "A very nice day";

        Element italic = paragraph.select("em").first();
        italic != null;
        italic.text() == "A very nice day";
    }

    def "two italic words within phrase"() {
        given:
        String input = "A _very nice_ day";

        when:
        Document doc = transform(input);

        then:
        Element paragraph = doc.select("div[class=paragraph] > p").first();
        paragraph != null;
        paragraph.text() == "A very nice day";

        Element bold = paragraph.select("em").first();
        bold != null;
        bold.text() == "very nice";
    }

    def "bold italic phrase"() {
        given:
        String input = "*_A very nice day_*";

        when:
        Document doc = transform(input);

        then:
        Element paragraph = doc.select("div[class=paragraph] > p").first();
        paragraph != null;
        paragraph.text() == "A very nice day";

        Element boldItalic = paragraph.select("strong > em").first();
        boldItalic != null;
        boldItalic.text() == "A very nice day";
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

    def "bold and italic letters within word"() {
        given:
        String input = "A ve*r*y n_ic_e day";

        when:
        Document doc = transform(input);

        then:
        Element paragraph = doc.select("div[class=paragraph] > p").first();
        paragraph != null;
        paragraph.text() == "A very nice day";

        Element bold = paragraph.select("strong").first();
        bold != null;
        bold.text() == "r";

        Element italic = paragraph.select("em").first();
        italic != null;
        italic.text() == "ic";
    }


}