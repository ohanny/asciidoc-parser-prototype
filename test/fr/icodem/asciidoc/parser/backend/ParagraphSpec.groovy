package fr.icodem.asciidoc.parser.backend

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class ParagraphSpec extends BackendBaseSpecification {

    def "one paragraph"() {
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

    def "two paragraphs"() {
        given:
        String input = "Three hours before the Abraham Lincoln " +
                       "left Brooklyn pier, I received a\n" +
                       "letter worded as follows:\n" +
                       "\n" +
                       "To M. ARONNAX, Professor in the Museum of Paris, " +
                       "Fifth Avenue Hotel,\n" + "New York.";

        when:
        Document doc = transform(input);

        then:
        Elements paragraphs = doc.select("div#content > div[class=paragraph]");
        paragraphs != null;
        paragraphs.size() == 2;

        Element div1 = paragraphs.first();
        div1 != null;

        div1.children().size() == 1;
        Element p1 = div1.child(0);
        p1.tagName() == "p";
        p1.text() == "Three hours before the Abraham Lincoln " +
                    "left Brooklyn pier, I received a " +
                    "letter worded as follows:";

        Element div2 = paragraphs.last();
        div2 != null;

        div2.children().size() == 1;
        Element p2 = div2.child(0);
        p2.tagName() == "p";
        p2.text() == "To M. ARONNAX, Professor in the Museum of Paris, " +
                     "Fifth Avenue Hotel, New York.";
    }
}