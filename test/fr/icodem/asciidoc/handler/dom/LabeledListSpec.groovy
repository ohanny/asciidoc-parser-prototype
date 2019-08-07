package fr.icodem.asciidoc.handler.dom

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.LabeledList
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ListBlock
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Paragraph

class LabeledListSpec extends DomHandlerBaseSpec {

    def "simple labeled list"() {
        given:
        String input = '''\
= Labeled List

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

        doc.content.sections[0].blocks[0] instanceof LabeledList
        LabeledList block = doc.content.sections[0].blocks[0]
        block.title == null
        block.type == ElementType.LabeledList
        block.items != null
        block.items.size() == 1

        block.items[0].title != null
        block.items[0].title.text == 'title'
        block.items[0].content != null
        block.items[0].content.content == 'content'

    }


}