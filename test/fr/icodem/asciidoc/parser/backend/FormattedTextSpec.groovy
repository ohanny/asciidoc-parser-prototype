package fr.icodem.asciidoc.parser.backend

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements

class FormattedTextSpec extends BackendBaseSpecification {

    def "bold phrase"() {
        given:
        String input = "*A very nice day*"

        when:
        Document doc = transform(input)

        then:
        doc.select("div[class=paragraph] > p > strong").size() == 1
        doc.select("div[class=paragraph] > p > strong").text() == "A very nice day"
    }

    def "two bold words within phrase"() {
        given:
        String input = "A *very nice* day"

        when:
        Document doc = transform(input)

        then:
        doc.select("div[class=paragraph] > p").size() == 1
        doc.select("div[class=paragraph] > p").text() == "A very nice day"

        doc.select("div[class=paragraph] > p > strong").size() == 1
        doc.select("div[class=paragraph] > p > strong").text() == "very nice"
    }

    def "italic phrase"() {
        given:
        String input = "_A very nice day_"

        when:
        Document doc = transform(input)

        then:
        doc.select("div[class=paragraph] > p > em").size() == 1
        doc.select("div[class=paragraph] > p > em").text() == "A very nice day"
    }

    def "two italic words within phrase"() {
        given:
        String input = "A _very nice_ day"

        when:
        Document doc = transform(input);

        then:
        doc.select("div[class=paragraph] > p").size() == 1
        doc.select("div[class=paragraph] > p").text() == "A very nice day"

        doc.select("div[class=paragraph] > p > em").size() == 1
        doc.select("div[class=paragraph] > p > em").text() == "very nice"
    }

    def "bold italic phrase"() {
        given:
        String input = "*_A very nice day_*"

        when:
        Document doc = transform(input)

        then:
        doc.select("div[class=paragraph] > p > strong > em").size() == 1
        doc.select("div[class=paragraph] > p > strong > em").text() == "A very nice day"
    }


    def "nested italic word within bold words"() {
        given:
        String input = "A *nice _and_ sunny* day"

        when:
        Document doc = transform(input)

        then:
        doc.select("div[class=paragraph] > p").size() == 1
        doc.select("div[class=paragraph] > p").text() == "A nice and sunny day"

        doc.select("div[class=paragraph] > p > strong").size() == 1
        doc.select("div[class=paragraph] > p > strong").text() == "nice and sunny"

        doc.select("div[class=paragraph] > p > strong > em").size() == 1
        doc.select("div[class=paragraph] > p > strong > em").text() == "and"
    }

    def "bold and italic letters within word"() {
        given:
        String input = "A ve*r*y n_ic_e day"

        when:
        Document doc = transform(input);

        then:
        doc.select("div[class=paragraph] > p").size() == 1
        doc.select("div[class=paragraph] > p").text() == "A very nice day"

        doc.select("div[class=paragraph] > p > strong").size() == 1
        doc.select("div[class=paragraph] > p > strong").text() == "r"

        doc.select("div[class=paragraph] > p > em").size() == 1
        doc.select("div[class=paragraph] > p > em").text() == "ic"
    }


}