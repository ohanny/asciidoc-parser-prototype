package fr.icodem.asciidoc.handler.dom

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.DocumentModelBuilder
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries
import spock.lang.Specification

class DomHandlerBaseSpec extends Specification {

    DocumentModelBuilder getBuilder() {
        AttributeEntries attributeEntries = AttributeEntries.newAttributeEntries()
        DocumentModelBuilder.newDocumentBuilder(attributeEntries)
    }

}