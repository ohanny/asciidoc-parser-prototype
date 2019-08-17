package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Paragraph
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Sidebar

class SidebarSpec extends DomHandlerBaseSpec {

    def "sidebar"() {
        given:
        String input = '''\
= Sidebar

== Section 1
****
This is some content
****
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
        doc.content.sections[0].blocks[0] instanceof Sidebar
        doc.content.sections[0].blocks[0].blocks != null
        doc.content.sections[0].blocks[0].blocks[0] instanceof Paragraph
        doc.content.sections[0].blocks[0].blocks[0].text.content == 'This is some content'
    }


}