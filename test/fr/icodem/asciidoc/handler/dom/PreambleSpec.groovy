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

        doc.preamble != null
        doc.preamble.blocks != null
        doc.preamble.blocks.size() == 1
        doc.preamble.blocks[0] instanceof Paragraph
        doc.preamble.blocks[0].text != null
        doc.preamble.blocks[0].text.content == 'This is some content'

        doc.sections == null


    }


}