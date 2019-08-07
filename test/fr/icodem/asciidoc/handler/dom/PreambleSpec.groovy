package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Paragraph
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Quote

class PreambleSpec extends DomHandlerBaseSpec {

    def "document with preamble"() {
        given:
        String input = '''\
= A document

This is some content

'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.content != null

        doc.content.preamble != null
        doc.content.preamble.blocks != null
        doc.content.preamble.blocks.size() == 1
        doc.content.preamble.blocks[0] instanceof Paragraph
        doc.content.preamble.blocks[0].text != null
        doc.content.preamble.blocks[0].text.content == 'This is some content'

    }


}