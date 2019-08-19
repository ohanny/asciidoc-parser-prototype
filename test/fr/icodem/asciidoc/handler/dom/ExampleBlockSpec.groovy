package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ExampleBlock
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Paragraph

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
        doc.content != null
        doc.content.sections != null
        doc.content.sections.size() == 1

        doc.content.sections[0].blocks != null
        doc.content.sections[0].blocks.size() == 1
        doc.content.sections[0].blocks[0] != null
        doc.content.sections[0].blocks[0] instanceof ExampleBlock
        doc.content.sections[0].blocks[0].blocks != null
        doc.content.sections[0].blocks[0].blocks[0] instanceof Paragraph
        doc.content.sections[0].blocks[0].blocks[0].text.source == 'This is some content'
    }

    def "example block with admonition"() {
        given:
        String input = '''\
= Example Block

== Section 1

[NOTE]
====
This is some content
====
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
        doc.content.sections[0].blocks[0] instanceof ExampleBlock
        doc.content.sections[0].blocks[0].blocks != null
        doc.content.sections[0].blocks[0].blocks[0] instanceof Paragraph
        doc.content.sections[0].blocks[0].blocks[0].text.source == 'This is some content'
        doc.content.sections[0].blocks[0].admonition == 'note'
    }


}