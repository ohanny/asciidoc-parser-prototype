package fr.icodem.asciidoc.renderer.html.section

import fr.icodem.asciidoc.renderer.html.HtmlRendererSpecification
import org.jsoup.nodes.Document

class HeadingsSpec extends HtmlRendererSpecification {

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