package fr.icodem.asciidoc.handler.dom

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.HorizontalRule

class HorizontalRuleSpec extends DomHandlerBaseSpec {

    def "horizontal rule between two paragraphs"() {
        given:
        String input = '''\
= Horizontal Rule

== Section 1
Paragraph 1

\'\'\'

Paragraph 2
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
        doc.content.sections[0].blocks.size() == 3
        doc.content.sections[0].blocks[1] != null
        doc.content.sections[0].blocks[1] instanceof HorizontalRule
        doc.content.sections[0].blocks[1].type == ElementType.HorizontalRule
    }


}