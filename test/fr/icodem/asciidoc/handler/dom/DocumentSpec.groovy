package fr.icodem.asciidoc.handler.dom

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.DocumentModelBuilder
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document

class DocumentSpec extends DomHandlerBaseSpec {

    def "empty document"() {
        given:
        DocumentModelBuilder builder = getBuilder()
        String input = "= "

        when:
        Document doc = builder.build(input)

        then:
        doc != null
    }

    def "document title"() {
        given:
        DocumentModelBuilder builder = getBuilder()
        String input = "= A title"

        when:
        Document doc = builder.build(input)

        then:
        doc != null
        doc.header != null
        doc.header.documentTitle != null
        doc.header.documentTitle.text == "A title"
    }

}