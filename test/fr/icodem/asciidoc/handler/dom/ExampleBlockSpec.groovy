package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ExampleBlock
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Paragraph

class ExampleBlockSpec extends DomHandlerBaseSpec {

    def "example block"() {
        given:
        String input = '''\
= Example Block

== Section 1
====
This is some content
====
'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.sections != null
        doc.sections.size() == 1

        doc.sections[0].blocks != null
        doc.sections[0].blocks.size() == 1
        doc.sections[0].blocks[0] != null
        doc.sections[0].blocks[0] instanceof ExampleBlock
        doc.sections[0].blocks[0].blocks != null
        doc.sections[0].blocks[0].blocks[0] instanceof Paragraph
        doc.sections[0].blocks[0].blocks[0].text.content == 'This is some content\n'
    }


}