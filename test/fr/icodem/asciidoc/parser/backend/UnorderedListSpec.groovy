package fr.icodem.asciidoc.parser.backend

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class UnorderedListSpec extends BackendBaseSpecification {

    def "unordered list with 3 items"() {
        given:
        String input = "* One\r\n" +
                       "* Two\r\n" +
                       "* Three"

        when:
        Document doc = transform(input)

        then:
        Elements elements = doc.select("div#content")
        elements.first() != null

        Element list = elements.first().child(0)
        list != null
        list.attr("class") == "ulist"

        Element ul = list.child(0)
        ul != null
        ul.tagName() == "ul"
        ul.children() != null
        ul.children().size() == 3
        ul.select("li > p")*.text() == ["One", "Two", "Three"]
    }

    def "two levels of nesting"() {
        given:
        String input = "* Kiwi\r\n" +
                       "* Apple\r\n" +
                       "** Golden\r\n" +
                       "** Granny Smith"

        when:
        Document doc = transform(input)

        then:
        Elements elements = doc.select("div#content")
        elements.first() != null

        Element list1 = elements.first().child(0)
        list1 != null
        list1.attr("class") == "ulist"

        Element ul1 = list1.child(0)
        ul1 != null
        ul1.tagName() == "ul"
        ul1.children() != null
        ul1.children().size() == 2
        ul1.select("li > p").take(2)*.text() == ["Kiwi", "Apple"]

        Element list2 = ul1.select("li > p + div").first()
        list2.attr("class") == "ulist"

        Element ul2 = list2.child(0)
        ul2 != null
        ul2.tagName() == "ul"
        ul2.children() != null
        ul2.children().size() == 2

        ul2.select("li > p")*.text() == ["Golden", "Granny Smith"]
    }
}