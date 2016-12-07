package fr.icodem.asciidoc.renderer.html.table

import fr.icodem.asciidoc.renderer.html.HtmlRendererSpecification
import org.jsoup.nodes.Document
import spock.lang.Ignore

class TableSpec extends HtmlRendererSpecification {

    //@Ignore("Until grammar is fixed")
    def "renders simple psv table"() {
        given:
        String input = '''\
|===
|A |B |C
|a |b |c
|1 |2 |3
|===
'''
        when:
        Document doc = transform(input)

        then:
        doc.select("div#content > div[class=paragraph]").size() == 1
        doc.select("div#content > div[class=paragraph] > p").size() == 1
        String text = '''\
Alice was beginning to get very tired of sitting by her sister on the bank.\
'''
        doc.select("div#content > div[class=paragraph] > p").first().text() == text
    }


}