package fr.icodem.asciidoc.renderer.html

import fr.icodem.asciidoc.parser.elements.AttributeEntry
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html.DefaultHtmlRenderer
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spock.lang.Specification

class HtmlRendererSpecification extends Specification {

    Document transform(String input) {
        transform(input, Collections.emptyList());
    }

    Document transform(String input, List<AttributeEntry> attributes) {
        StringWriter writer = new StringWriter()
        DefaultHtmlRenderer.withWriter(writer).render(input)
        Jsoup.parse(writer.toString(), "UTF-8")
    }

}