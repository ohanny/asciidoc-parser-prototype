package fr.icodem.asciidoc.renderer.html.block

import fr.icodem.asciidoc.renderer.html.HtmlRendererSpecification
import org.jsoup.nodes.Document

class HorizontalRuleSpec extends HtmlRendererSpecification {

    def "horizontal rule"() {
        given:
        String input = '''\
\'''\
'''

        when:
        Document doc = transform(input)

        then:
        doc.select("hr").size() == 1
        doc.select("div#content > hr").size() == 1
    }

    def "horizontal rule between blocks"() {
        given:
        String input = '''\
Block above

\'''\

Block below
'''

        when:
        Document doc = transform(input)

        then:
        doc.select("hr").size() == 1
        doc.select("div#content > div.paragraph + hr").size() == 1
        doc.select("div.paragraph:nth-child(1) > p").text() == "Block above"
        doc.select("div#content > hr + div.paragraph").size() == 1
        doc.select("div.paragraph:nth-child(3) > p").text() == "Block below"
    }


}