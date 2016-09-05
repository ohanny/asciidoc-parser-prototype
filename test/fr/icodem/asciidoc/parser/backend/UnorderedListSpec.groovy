package fr.icodem.asciidoc.parser.backend

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class UnorderedListSpec extends BackendBaseSpecification {

    def "unordered list with 3 items"() {
        given:
        String input = "* One\r\n" +
                       "* Two\r\n" +
                       "* Three";

        when:
        Document doc = transform(input);

        then:
        Elements elements = doc.select("div#content");
        elements.first() != null;

        Element list = elements.first().child(0);
        list != null;
        list.attr("class") == "ulist";

        Element ul = list.child(0);
        ul != null;
        ul.tagName() == "ul";
        ul.children() != null;
        ul.children().size() == 3;
        ul.select("li > p")*.text() == ["One", "Two", "Three"];
    }
}