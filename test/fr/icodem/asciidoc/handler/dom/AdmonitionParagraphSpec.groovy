package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document

class AdmonitionParagraphSpec extends DomHandlerBaseSpec {

    def "section with two paragraphs"() {
        given:
        String input = '''\
= Paragraph with admonition

== Section 1
NOTE: This is a note


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
        doc.sections[0].blocks[0].text != null
        doc.sections[0].blocks[0].text.content == 'This is a note'
        doc.sections[0].blocks[0].admonition == 'note'

    }


}