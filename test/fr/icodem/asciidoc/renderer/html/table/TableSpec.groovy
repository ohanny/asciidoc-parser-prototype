package fr.icodem.asciidoc.renderer.html.table

import fr.icodem.asciidoc.renderer.html.HtmlRendererSpecification
import org.jsoup.nodes.Document
import spock.lang.Ignore

class TableSpec extends HtmlRendererSpecification {

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
        doc.select("table.tableblock.frame-all.grid-all.spread").size() == 1
        doc.select("table > colgroup > col[style*=\"width: 33.333333333333336%\"]").size() == 3
        doc.select("table tr").size() == 3
        doc.select("table > tbody > tr").size() == 3
        doc.select("table td").size() == 9
        doc.select("table > tbody > tr > td.tableblock.halign-left.valign-top > p.tableblock").size() == 9
        1.upto(3, {
            assert doc.select("table > tbody > tr:nth-child(${it}) > td").size() == 3
            assert doc.select("table > tbody > tr:nth-child(${it}) > td > p").size() == 3
        })
        doc.select("table > tbody > tr:nth-child(1) > td > p")*.text() == ['A', 'B', 'C']
        doc.select("table > tbody > tr:nth-child(2) > td > p")*.text() == ['a', 'b', 'c']
        doc.select("table > tbody > tr:nth-child(3) > td > p")*.text() == ['1', '2', '3']
    }

}