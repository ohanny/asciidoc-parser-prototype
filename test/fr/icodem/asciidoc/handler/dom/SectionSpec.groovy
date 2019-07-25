package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document

class SectionSpec extends DomHandlerBaseSpec {

    def "a document title and a few section titles - no blank line between titles"() {
        given:
        String input = '''\
= Document Title
== First Section
=== Section 1.1
=== Section 1.2
== Second Section
=== Section 2.1
=== Section 2.2
'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.header.documentTitle != null
        doc.header.documentTitle.text == 'Document Title'
        doc.sections != null
        doc.sections.size() == 6

        doc.sections[0].level == 2
        doc.sections[0].title != null
        doc.sections[0].title.text == 'First Section'

        doc.sections[1].level == 3
        doc.sections[1].title != null
        doc.sections[1].title.text == 'Section 1.1'

        doc.sections[2].level == 3
        doc.sections[2].title != null
        doc.sections[2].title.text == 'Section 1.2'

        doc.sections[3].level == 2
        doc.sections[3].title != null
        doc.sections[3].title.text == 'Second Section'

        doc.sections[4].level == 3
        doc.sections[4].title != null
        doc.sections[4].title.text == 'Section 2.1'

        doc.sections[5].level == 3
        doc.sections[5].title != null
        doc.sections[5].title.text == 'Section 2.2'
    }


}