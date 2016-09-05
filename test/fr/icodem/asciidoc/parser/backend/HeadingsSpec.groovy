package fr.icodem.asciidoc.parser.backend

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class HeadingsSpec extends BackendBaseSpecification {

    def "document title"() {
        given:
        String input = "= A title";

        when:
        Document doc = transform(input);

        then:
        Elements elements = doc.select("body");
        elements.first() != null;

        Element header = elements.first().child(0);
        header != null;
        header.children().size() == 1;
        header.child(0).tagName() == "h1";
        header.child(0).text() == "A title";
    }
}