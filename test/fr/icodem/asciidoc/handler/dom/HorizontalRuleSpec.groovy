package fr.icodem.asciidoc.handler.dom

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.HorizontalRule
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.LiteralBlock

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
        doc.sections != null
        doc.sections.size() == 1

        doc.sections[0].blocks != null
        doc.sections[0].blocks.size() == 3
        doc.sections[0].blocks[1] != null
        doc.sections[0].blocks[1] instanceof HorizontalRule
        doc.sections[0].blocks[1].type == ElementType.HorizontalRule
    }


}