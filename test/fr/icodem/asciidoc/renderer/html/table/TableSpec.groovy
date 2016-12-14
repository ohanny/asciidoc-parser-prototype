package fr.icodem.asciidoc.renderer.html.table

import fr.icodem.asciidoc.renderer.html.HtmlRendererSpecification
import org.jsoup.nodes.Document
import spock.lang.Ignore

class TableSpec extends HtmlRendererSpecification {

    def "renders simple psv table"() {
        given:
        String input = '''\
|=======
|A |B |C
|a |b |c
|1 |2 |3
|=======
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

    def "table with implicit header row"() {
        given:
        String input = '''\
|===
|Column 1 |Column 2

|Data A1
|Data B1

|Data A2
|Data B2
|===
'''
        when:
        Document doc = transform(input)

        then:
        doc.select("table.tableblock.frame-all.grid-all.spread").size() == 1
        doc.select("table > colgroup > col").size() == 2
        doc.select("table > colgroup > col[style*=\"width: 50.0%\"]").size() == 2
        doc.select("table > thead > tr").size() == 1
        doc.select("table > thead > tr").size() == 1
        doc.select("table > thead > tr > th").size() == 2
        doc.select("table > thead > tr > th")*.text() == ['Column 1', 'Column 2']
        doc.select("table > tbody").size() == 1
        doc.select("table > tbody > tr").size() == 2
        doc.select("table td").size() == 4
        doc.select("table > tbody > tr > td.tableblock.halign-left.valign-top > p.tableblock").size() == 4
        1.upto(2, {
            assert doc.select("table > tbody > tr:nth-child(${it}) > td").size() == 2
            assert doc.select("table > tbody > tr:nth-child(${it}) > td > p").size() == 2
        })
        doc.select("table > tbody > tr:nth-child(1) > td > p")*.text() == ['Data A1', 'Data B1']
        doc.select("table > tbody > tr:nth-child(2) > td > p")*.text() == ['Data A2', 'Data B2']
    }

    def "table and col width not assigned when autowidth option is specified"() {
        given:
        String input = '''\
[options="autowidth"]
|=======
|A |B |C
|a |b |c
|1 |2 |3
|=======
'''
        when:
        Document doc = transform(input)

        then:
        doc.select("table.tableblock.frame-all.grid-all").size() == 1
        doc.select("table.tableblock.frame-all.grid-all.spread").size() == 0
        doc.select("table[style*=\"width\"]").size() == 0
        doc.select("table colgroup col").size() == 3
        doc.select("table colgroup col[style*=\"width\"]").size() == 0
    }

    def "table with implicit header row when other options set"() {
        given:
        String input = '''\
[%autowidth]
|===
|Column 1 |Column 2

|Data A1
|Data B1
|===
'''
        when:
        Document doc = transform(input)

        then:
        doc.select("table.tableblock.frame-all.grid-all").size() == 1
        doc.select("table.tableblock.frame-all.grid-all.spread").size() == 0
        doc.select("table[style*=\"width\"]").size() == 0
        doc.select("table colgroup col").size() == 2
        doc.select("table > thead").size() == 1
        doc.select("table > thead  > tr").size() == 1
        doc.select("table > thead > tr > th").size() == 2
        doc.select("table > tbody").size() == 1
        doc.select("table > tbody > tr").size() == 1
    }
}
