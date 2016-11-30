package fr.icodem.asciidoc.parser.backend

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class ParagraphSpec extends BackendBaseSpecification {

    def "one paragraph"() {
        given:
        String input = '''\
Alice was beginning to get very tired of sitting by her sister on the bank.\
'''

        when:
        Document doc = transform(input)

        then:
        doc.select("div#content > div[class=paragraph]").size() == 1
        doc.select("div#content > div[class=paragraph] > p").size() == 1
        String text = '''\
Alice was beginning to get very tired of sitting by her sister on the bank.\
'''
        doc.select("div#content > div[class=paragraph] > p").first().text() == text
    }

    def "two paragraphs"() {
        given:
        String input = '''
Three hours before the Abraham Lincoln left Brooklyn pier, I received a
letter worded as follows:

To M. ARONNAX, Professor in the Museum of Paris, Fifth Avenue Hotel,
New York.
'''

        when:
        Document doc = transform(input);

        then:
        doc.select("div#content > div[class=paragraph]").size() == 2
        doc.select("div#content > div[class=paragraph] > p").size() == 2

        String text1 = '''\
Three hours before the Abraham Lincoln left Brooklyn pier, I received a letter worded as follows:\
'''
        doc.select("div#content > div[class=paragraph]:nth-child(1) > p").first().text() == text1

        String text2 = '''\
To M. ARONNAX, Professor in the Museum of Paris, Fifth Avenue Hotel, New York.\
'''
        doc.select("div#content > div[class=paragraph]:nth-child(2) > p").first().text() == text2
    }
}