package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ImageBlock
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Paragraph

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

    def "document with header, attribute and content"() {
        given:
        String input = '''
= A title
John Doe <jd@mail.com>
v1.0
:icons: font

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

        doc.attributes != null
        doc.attributes.getAttribute('icons') != null
        doc.attributes.getValue('icons') == 'font'

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

    def "document with title, attribute and content"() {
        given:
        String input = '''
= A title
:icons: font

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
        doc.header.authors.empty
        doc.header.revisionInfo == null

        doc.attributes != null
        doc.attributes.getAttribute('icons') != null
        doc.attributes.getValue('icons') == 'font'

        doc.content != null
        doc.content.preamble == null

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

    def "document with title, attribute entries, preamble, section, paragraph and attribute lists"() {
        given:
        String input = '''
= A title
:imagesdir: images
:icons: font

Some content

[%option1.role1]
== Section 1

[.role2.role3,att1=value1,att2=value2]
Other content

[.role4.role5]
More content

[.role6]
image::myimage.png[]

'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null

        doc.header.documentTitle != null
        doc.header.documentTitle.text == 'A title'

        doc.header.authors != null
        doc.header.authors.empty
        doc.header.revisionInfo == null

        doc.attributes != null
        doc.attributes.getAttribute('icons') != null
        doc.attributes.getValue('icons') == 'font'

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
        doc.content.sections[0].attributes != null
        doc.content.sections[0].attributes.hasOption('option1')
        doc.content.sections[0].attributes.hasRole('role1')
        doc.content.sections[0].type == ElementType.Section
        doc.content.sections[0].level == 2
        doc.content.sections[0].title != null
        doc.content.sections[0].title.text == 'Section 1'
        doc.content.sections[0].blocks != null
        doc.content.sections[0].blocks.size() == 3

        doc.content.sections[0].blocks[0].attributes != null
        doc.content.sections[0].blocks[0].attributes.hasRole('role2')
        doc.content.sections[0].blocks[0].attributes.hasRole('role3')
        doc.content.sections[0].blocks[0].attributes.getStringValue('att1') == 'value1'
        doc.content.sections[0].blocks[0].attributes.getStringValue('att2') == 'value2'
        doc.content.sections[0].blocks[0] instanceof Paragraph
        doc.content.sections[0].blocks[0].type == ElementType.Paragraph
        doc.content.sections[0].blocks[0].admonition == null
        doc.content.sections[0].blocks[0].text != null
        doc.content.sections[0].blocks[0].text.content == 'Other content'

        doc.content.sections[0].blocks[1].attributes != null
        doc.content.sections[0].blocks[1].attributes.hasRole('role4')
        doc.content.sections[0].blocks[1].attributes.hasRole('role5')
        doc.content.sections[0].blocks[1] instanceof Paragraph
        doc.content.sections[0].blocks[1].type == ElementType.Paragraph
        doc.content.sections[0].blocks[1].admonition == null
        doc.content.sections[0].blocks[1].text != null
        doc.content.sections[0].blocks[1].text.content == 'More content'

        doc.content.sections[0].blocks[2].attributes != null
        doc.content.sections[0].blocks[2].attributes.hasRole('role6')
        doc.content.sections[0].blocks[2] instanceof ImageBlock
        doc.content.sections[0].blocks[2].type == ElementType.ImageBlock
        doc.content.sections[0].blocks[2].source == 'images/myimage.png'
        doc.content.sections[0].blocks[2].alternateText == '/myimage'
    }

}