package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.LiteralBlock

class LiteralBlockSpec extends DomHandlerBaseSpec {

    def "literal block"() {
        given:
        String input = '''\
= Literal Block

== Section 1
....
This is some content
....
'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.content != null
        doc.content.sections != null
        doc.content.sections.size() == 1

        doc.content.sections[0].blocks != null
        doc.content.sections[0].blocks.size() == 1
        doc.content.sections[0].blocks[0] != null
        doc.content.sections[0].blocks[0] instanceof LiteralBlock
        doc.content.sections[0].blocks[0].text != null
        doc.content.sections[0].blocks[0].text.content == 'This is some content\n'
    }


}