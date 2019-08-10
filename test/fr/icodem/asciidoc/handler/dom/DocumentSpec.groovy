package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Paragraph

class DocumentSpec extends DomHandlerBaseSpec {

    def "empty document"() {
        given:
        String input = "= "

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
    }

    def "document title"() {
        given:
        String input = "= A title"

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.header.documentTitle != null
        doc.header.documentTitle.text == 'A title'
    }

    def "document with header and content"() {
        given:
        String input = '''
= A title
John Doe <jd@mail.com>
v1.0

Some content

== Section 1
   
Other content
'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null

        doc.header.documentTitle != null
        doc.header.documentTitle.text == 'A title'

        doc.header.authors != null
        doc.header.authors.size() == 1
        doc.header.authors[0] != null
        doc.header.authors[0].firstName == 'John'
        doc.header.authors[0].lastName == 'Doe'
        doc.header.authors[0].email == 'jd@mail.com'

        doc.header.revisionInfo != null
        doc.header.revisionInfo.date == 'v1.0'

        doc.content != null
        doc.content.preamble != null
        doc.content.preamble.blocks != null
        doc.content.preamble.blocks.size() == 1
        doc.content.preamble.blocks[0] != null
        doc.content.preamble.blocks[0] instanceof Paragraph
        doc.content.preamble.blocks[0].type == ElementType.Paragraph
        doc.content.preamble.blocks[0].admonition == null
        doc.content.preamble.blocks[0].text != null
        doc.content.preamble.blocks[0].text.content == 'Some content'

        doc.content.sections != null
        doc.content.sections.size() == 1
        doc.content.sections[0] != null
        doc.content.sections[0].type == ElementType.Section
        doc.content.sections[0].level == 2
        doc.content.sections[0].title != null
        doc.content.sections[0].title.text == 'Section 1'
        doc.content.sections[0].blocks != null
        doc.content.sections[0].blocks.size() == 1
        doc.content.sections[0].blocks[0] instanceof Paragraph
        doc.content.sections[0].blocks[0].type == ElementType.Paragraph
        doc.content.sections[0].blocks[0].admonition == null
        doc.content.sections[0].blocks[0].text != null
        doc.content.sections[0].blocks[0].text.content == 'Other content'


    }

}