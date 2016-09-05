package fr.icodem.asciidoc.parser.backend

import fr.icodem.asciidoc.backend.html.HtmlBackend
import fr.icodem.asciidoc.parser.AsciidocPegProcessor
import fr.icodem.asciidoc.parser.elements.AttributeEntry
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spock.lang.Specification

class BackendBaseSpecification extends Specification {

    Document convert(String input) {
        convert(input, Collections.emptyList());
    }

    Document convert(String input, List<AttributeEntry> attributes) {
        StringWriter writer = new StringWriter();
        new AsciidocPegProcessor(new HtmlBackend(writer), attributes).parse(input);
        println writer.toString();
        Jsoup.parse(writer.toString(), "UTF-8");
    }

}