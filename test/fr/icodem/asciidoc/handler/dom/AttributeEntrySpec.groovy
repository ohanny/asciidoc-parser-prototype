package fr.icodem.asciidoc.handler.dom

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.DocumentModelBuilder
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntry
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document

class AttributeEntrySpec extends DomHandlerBaseSpec {

    def "default attribute entry"() {
        given:
        String input = ""

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.header.attributes != null
        AttributeEntry att = doc.header.attributes.getAttribute("doctitle")
        att.name == "doctitle"
        att.value == "Untitled"
        att.disabled == false
    }

    def "one attribute entry"() {
        given:
        String input = ":att1: value1"

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.header.attributes != null
        AttributeEntry att = doc.header.attributes.getAttribute("att1")
        att.name == "att1"
        att.value == "value1"
        att.disabled == false
    }

    def "two attribute entries"() {
        given:
        String input = '''\
:att1: value1
:att2: value2
'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.header.attributes != null

        AttributeEntry att1 = doc.header.attributes.getAttribute("att1")
        att1.name == "att1"
        att1.value == "value1"
        att1.disabled == false

        AttributeEntry att2 = doc.header.attributes.getAttribute("att2")
        att2.name == "att2"
        att2.value == "value2"
        att2.disabled == false
    }

    def "unset attribute with leading bang"() {
        given:
        String input = '''\
:!doctitle:
'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.header.attributes != null

        AttributeEntry att = doc.header.attributes.getAttribute("doctitle")
        att.name == "doctitle"
        att.disabled == true

    }

}