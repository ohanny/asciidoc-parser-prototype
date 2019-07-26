package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document

class AttributeListSpec extends DomHandlerBaseSpec {

    def "paragraph with attribute list"() {
        given:
        String input = '''\
= Attribute List Example

== Section 1

[#tiger.summary.incremental%header%footer,att1=value1,att2=value2]
This is some content

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
        doc.sections[0].blocks[0].text.content == 'This is some content'
        doc.sections[0].blocks[0].admonition == null

        doc.sections[0].blocks[0].attributeList != null
        doc.sections[0].blocks[0].attributeList.id == 'tiger'


    }


}