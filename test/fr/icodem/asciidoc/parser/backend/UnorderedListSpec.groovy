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
        println doc.select("div.ulist:nth-child(2) > ul > li")
        doc.select("div.ulist").size() == 2
        doc.select("div.ulist > ul").size() == 2
        doc.select("div.ulist:nth-child(1) > ul > li").size() == 2
        doc.select("div.ulist:nth-child(1) > ul > li > p")*.text() == ["Foo", "Blech"]
        doc.select("div.ulist:nth-child(2) > ul > li").size() == 1
        doc.select("div.ulist:nth-child(2) > ul > li > p")*.text() == ["Boo"]
    }

    def "three levels of nestingx"() {
        given:
        String input = "* Fruit\r\n" +
                "** Apple\r\n" +
                "*** Granny Smith"

        when:
        Document doc = transform(input)

        then:
        Elements elements = doc.select("div#content")
    }


    def "three levels of nesting"() {
        given:
        String input = "* Fruit\r\n" +
                       "** Apple\r\n" +
                       "*** Granny Smith"

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
        ul1.children().size() == 1
        ul1.select("li > p").take(1)*.text() == ["Fruit"]

        Element list2 = ul1.select("li > p + div").first()
        list2.attr("class") == "ulist"

        Element ul2 = list2.child(0)
        ul2 != null
        ul2.tagName() == "ul"
        ul2.children() != null
        ul2.children().size() == 1

        ul2.select("li > p").take(1)*.text() == ["Apple"]

        Element list3 = ul2.select("li > p + div").first()
        list3.attr("class") == "ulist"

        Element ul3 = list3.child(0)
        ul3 != null
        ul3.tagName() == "ul"
        ul3.children() != null
        ul3.children().size() == 1

        ul3.select("li > p")*.text() == ["Granny Smith"]
    }

}