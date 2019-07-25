package fr.icodem.asciidoc.handler.dom

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.DocumentModelBuilder
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Author
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document

class RevisionInfoSpec extends DomHandlerBaseSpec {

    def "header with one author name"() {
        given:
        DocumentModelBuilder builder = getBuilder()
        String input = '''\
= Hello, AsciiDoc!
John Doe
v1.0, April 2, 2019: Some remark
'''

        when:
        Document doc = builder.build(input)

        then:
        doc != null
        doc.header != null
        doc.header.revisionInfo != null
        doc.header.revisionInfo.date == "v1.0, April 2, 2019: Some remark"

    }


}