package fr.icodem.asciidoc.parser.backend

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class HeadingsSpec extends BackendBaseSpecification {

    def "document title"() {
        given:
        String input = "= A title"

        when:
        Document doc = transform(input)

        then:
        doc.select("body > div#header").size() == 1
        doc.select("body > div#header > h1").size() == 1
        doc.select("body > div#header > h1").text() == "A title"
    }
}