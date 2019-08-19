package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Paragraph
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Quote

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
        doc.content != null
        doc.content.sections != null
        doc.content.sections.size() == 1

        doc.content.sections[0].blocks != null
        doc.content.sections[0].blocks.size() == 2
        doc.content.sections[0].blocks[0] != null
        doc.content.sections[0].blocks[0] instanceof Paragraph
        doc.content.sections[0].blocks[0].text != null
        doc.content.sections[0].blocks[0].text.source == 'This is some content'
        doc.content.sections[0].blocks[0].admonition == null
        doc.content.sections[0].blocks[1] != null
        doc.content.sections[0].blocks[0] instanceof Paragraph
        doc.content.sections[0].blocks[1].text != null
        doc.content.sections[0].blocks[1].text.source == 'This is other content'
        doc.content.sections[0].blocks[1].admonition == null

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
        doc.content != null
        doc.content.sections != null
        doc.content.sections.size() == 2

        doc.content.sections[0].blocks != null
        doc.content.sections[0].blocks.size() == 1
        doc.content.sections[0].blocks[0] != null
        doc.content.sections[0].blocks[0] instanceof Paragraph
        doc.content.sections[0].blocks[0].text != null
        doc.content.sections[0].blocks[0].text.source == 'This is some content'
        doc.content.sections[0].blocks[0].admonition == null

        doc.content.sections[1].blocks != null
        doc.content.sections[1].blocks.size() == 1
        doc.content.sections[1].blocks[0] != null
        doc.content.sections[0].blocks[0] instanceof Paragraph
        doc.content.sections[1].blocks[0].text != null
        doc.content.sections[1].blocks[0].text.source == 'This is other content'
        doc.content.sections[1].blocks[0].admonition == null

    }

    def "quoted paragraph"() {
        given:
        String input = '''\
= Paragraph Example

== Section 1

[quote, attribution, citation title and information]
This is some content

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

        doc.content.sections[0].blocks[0] instanceof Quote == true
        doc.content.sections[0].blocks[0].text != null
        doc.content.sections[0].blocks[0].text.source == 'This is some content'
        doc.content.sections[0].blocks[0].attribution == 'attribution'
        doc.content.sections[0].blocks[0].citationTitle == 'citation title and information'

    }


}