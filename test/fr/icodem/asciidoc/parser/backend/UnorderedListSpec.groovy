package fr.icodem.asciidoc.parser.backend

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class UnorderedListSpec extends BackendBaseSpecification {

    def "asterisk elements with no blank lines"() {
        given:
        String input = '''\
* Foo
* Boo
* Blech
'''

        when:
        Document doc = transform(input)

        then:
        doc.select("div.ulist").size() == 1
        doc.select("div.ulist > ul").size() == 1
        doc.select("div.ulist > ul > li").size() == 3
        doc.select("div.ulist > ul > li > p")*.text() == ["Foo", "Boo", "Blech"]
    }

    def "nested elements (2) with asterisks"() {
        given:
        String input = '''\
* Foo
** Boo
* Blech
'''

        when:
        Document doc = transform(input)

        then:
        doc.select("div.ulist").size() == 2
        doc.select("div.ulist > ul").size() == 2
        doc.select("div#content > div.ulist > ul > li").size() == 2
        doc.select("div#content > div.ulist > ul > li > p")*.text() == ["Foo", "Blech"]
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li").size() == 1
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > p")*.text() == ["Boo"]
    }

    def "nested elements (3) with asterisks"() {
        given:
        String input = '''\
* Foo
** Boo
*** Snoo
* Blech
'''

        when:
        Document doc = transform(input)

        then:
        doc.select("div.ulist").size() == 3
        doc.select("div.ulist > ul").size() == 3
        doc.select("div#content > div.ulist > ul > li").size() == 2
        doc.select("div#content > div.ulist > ul > li > p")*.text() == ["Foo", "Blech"]
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li").size() == 1
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > p")*.text() == ["Boo"]
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li").size() == 1
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > p")*.text() == ["Snoo"]
    }


    def "nested elements (4) with asterisks"() {
        given:
        String input = '''\
* Foo
** Boo
*** Snoo
**** Froo
* Blech
'''

        when:
        Document doc = transform(input)

        then:
        doc.select("div.ulist").size() == 4
        doc.select("div.ulist > ul").size() == 4
        doc.select("div#content > div.ulist > ul > li").size() == 2
        doc.select("div#content > div.ulist > ul > li > p")*.text() == ["Foo", "Blech"]
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li").size() == 1
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > p")*.text() == ["Boo"]
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li").size() == 1
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > p")*.text() == ["Snoo"]
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li").size() == 1
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > p")*.text() == ["Froo"]
    }

    def "nested elements (5) with asterisks"() {
        given:
        String input = '''\
* Foo
** Boo
*** Snoo
**** Froo
***** Groo
* Blech
'''

        when:
        Document doc = transform(input)

        then:
        doc.select("div.ulist").size() == 5
        doc.select("div.ulist > ul").size() == 5
        doc.select("div#content > div.ulist > ul > li").size() == 2
        doc.select("div#content > div.ulist > ul > li > p")*.text() == ["Foo", "Blech"]
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li").size() == 1
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > p")*.text() == ["Boo"]
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li").size() == 1
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > p")*.text() == ["Snoo"]
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li").size() == 1
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > p")*.text() == ["Froo"]
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li").size() == 1
        doc.select("div#content > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > div.ulist > ul > li > p")*.text() == ["Groo"]
    }

}