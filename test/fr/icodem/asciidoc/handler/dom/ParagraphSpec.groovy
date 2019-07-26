package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document

class ParagraphSpec extends DomHandlerBaseSpec {

    def "section with two paragraphs"() {
        given:
        String input = '''\
= Paragraph Example

== Section 1
This is some content

This is other content

'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.sections != null
        doc.sections.size() == 1

        doc.sections[0].blocks != null
        doc.sections[0].blocks.size() == 2
        doc.sections[0].blocks[0] != null
        doc.sections[0].blocks[0].text != null
        doc.sections[0].blocks[0].text.content == 'This is some content'
        doc.sections[0].blocks[0].admonition == null
        doc.sections[0].blocks[1] != null
        doc.sections[0].blocks[1].text != null
        doc.sections[0].blocks[1].text.content == 'This is other content'
        doc.sections[0].blocks[1].admonition == null

    }

    def "two sections with one paragraph"() {
        given:
        String input = '''\
= Paragraph Example

== Section 1
This is some content

== Section 2
This is other content

'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.sections != null
        doc.sections.size() == 2

        doc.sections[0].blocks != null
        doc.sections[0].blocks.size() == 1
        doc.sections[0].blocks[0] != null
        doc.sections[0].blocks[0].text != null
        doc.sections[0].blocks[0].text.content == 'This is some content'
        doc.sections[0].blocks[0].admonition == null

        doc.sections[1].blocks != null
        doc.sections[1].blocks.size() == 1
        doc.sections[1].blocks[0] != null
        doc.sections[1].blocks[0].text != null
        doc.sections[1].blocks[0].text.content == 'This is other content'
        doc.sections[1].blocks[0].admonition == null

    }


}