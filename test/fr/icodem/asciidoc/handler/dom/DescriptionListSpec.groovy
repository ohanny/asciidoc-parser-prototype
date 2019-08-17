package fr.icodem.asciidoc.handler.dom

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.DescriptionList

class DescriptionListSpec extends DomHandlerBaseSpec {

    def "simple description list"() {
        given:
        String input = '''\
= Description List

== Section 1

title:: content
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

        doc.content.sections[0].blocks[0] instanceof DescriptionList
        DescriptionList block = doc.content.sections[0].blocks[0]
        block.title == null
        block.type == ElementType.DescriptionList
        block.items != null
        block.items.size() == 1

        block.items[0].title != null
        block.items[0].title.text == 'title'
        block.items[0].content != null
        block.items[0].content.content == 'content'

    }


}