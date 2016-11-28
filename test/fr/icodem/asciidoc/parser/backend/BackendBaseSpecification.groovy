package fr.icodem.asciidoc.parser.backend

import fr.icodem.asciidoc.parser.elements.AttributeEntry
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.DefaultHtmlRenderer
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spock.lang.Specification

class BackendBaseSpecification extends Specification {

    Document transform(String input) {
        transform(input, Collections.emptyList());
    }

    Document transform(String input, List<AttributeEntry> attributes) {
        StringWriter writer = new StringWriter()
        DefaultHtmlRenderer.withWriter(writer).render(input)
        Jsoup.parse(writer.toString(), "UTF-8")
    }

}