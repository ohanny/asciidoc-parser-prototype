package fr.icodem.asciidoc.renderer.html.list

import fr.icodem.asciidoc.renderer.html.HtmlRendererSpecification
import org.jsoup.nodes.Document

class OrderedListSpec extends HtmlRendererSpecification {

    def "dot elements with no blank lines"() {
        given:
        String input = '''\
. Foo
. Boo
. Blech
'''
        when:
        Document doc = transform(input)

        then:
        doc.select("div.olist").size() == 1
        doc.select("div.olist > ol").size() == 1
        doc.select("div.olist > ol > li").size() == 3
        doc.select("div.olist > ol > li > p")*.text() == ["Foo", "Boo", "Blech"]
    }

    def "nested ordered elements (2)"() {
        given:
        String input = '''\
. Foo
.. Boo
. Blech
'''
        when:
        Document doc = transform(input)

        then:
        doc.select("div.olist").size() == 2
        doc.select("div.olist > ol").size() == 2
        doc.select("div#content > div.olist > ol > li").size() == 2
        doc.select("div#content > div.olist > ol > li > p")*.text() == ["Foo", "Blech"]
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li").size() == 1
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > p")*.text() == ["Boo"]
    }

    def "nested ordered elements (3)"() {
        given:
        String input = '''\
. Foo
.. Boo
... Snoo
. Blech
'''
        when:
        Document doc = transform(input)

        then:
        doc.select("div.olist").size() == 3
        doc.select("div.olist > ol").size() == 3
        doc.select("div#content > div.olist > ol > li").size() == 2
        doc.select("div#content > div.olist > ol > li > p")*.text() == ["Foo", "Blech"]
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li").size() == 1
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > p")*.text() == ["Boo"]
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li").size() == 1
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > p")*.text() == ["Snoo"]
    }


    def "nested ordered elements (4)"() {
        given:
        String input = '''\
. Foo
.. Boo
... Snoo
.... Froo
. Blech
'''
        when:
        Document doc = transform(input)

        then:
        doc.select("div.olist").size() == 4
        doc.select("div.olist > ol").size() == 4
        doc.select("div#content > div.olist > ol > li").size() == 2
        doc.select("div#content > div.olist > ol > li > p")*.text() == ["Foo", "Blech"]
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li").size() == 1
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > p")*.text() == ["Boo"]
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li").size() == 1
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > p")*.text() == ["Snoo"]
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li").size() == 1
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > p")*.text() == ["Froo"]
    }

    def "nested ordered elements (5)"() {
        given:
        String input = '''\
. Foo
.. Boo
... Snoo
.... Froo
..... Groo
. Blech
'''
        when:
        Document doc = transform(input)

        then:
        doc.select("div.olist").size() == 5
        doc.select("div.olist > ol").size() == 5
        doc.select("div#content > div.olist > ol > li").size() == 2
        doc.select("div#content > div.olist > ol > li > p")*.text() == ["Foo", "Blech"]
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li").size() == 1
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > p")*.text() == ["Boo"]
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li").size() == 1
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > p")*.text() == ["Snoo"]
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li").size() == 1
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > p")*.text() == ["Froo"]
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li").size() == 1
        doc.select("div#content > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > div.olist > ol > li > p")*.text() == ["Groo"]
    }

}