package fr.icodem.asciidoc.parser.backend

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class ParagraphSpec extends BackendBaseSpecification {

    def "paragraph"() {
        given:
        String input = "Alice was beginning to get very tired " +
                       "of sitting by her sister on the bank.";

        when:
        Document doc = transform(input);

        then:
        Element div = doc.select("div#content > div[class=paragraph]").first();
        div != null;

        div.children().size() == 1;
        Element p = div.child(0);
        p.tagName() == "p";
        p.text() == "Alice was beginning to get very tired " +
                    "of sitting by her sister on the bank.";

    }
}