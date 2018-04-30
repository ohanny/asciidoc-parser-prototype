package fr.icodem.asciidoc.parser.listing

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.Listing
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.CodePoint
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.HighlightParameter
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.ListingProcessor
import spock.lang.Specification


class ListingProcessorSpec extends Specification {
    def processor = new ListingProcessor()

    def "listing should not be modified" () {
        given:
        String input = '''\
public class Item {
    private int id;
    private String name;
}
'''

        when:
        Listing listing = processor.process(input.chars, false, null, false, false, null)

        then:
        listing != null
        listing.lines.size() == 4

        listing.lines[0].number == 1
        listing.lines[0].text == "public class Item {"
        listing.lines[0].lineChunks != null
        listing.lines[0].lineChunks.size() == 1
        listing.lines[0].lineChunks[0].text == "public class Item {"

        listing.lines[1].number == 2
        listing.lines[1].text == "    private int id;"
        listing.lines[1].lineChunks != null
        listing.lines[1].lineChunks.size() == 1
        listing.lines[1].lineChunks[0].text == "    private int id;"

        listing.lines[2].number == 3
        listing.lines[2].text == "    private String name;"
        listing.lines[2].lineChunks != null
        listing.lines[2].lineChunks.size() == 1
        listing.lines[2].lineChunks[0].text == "    private String name;"

        listing.lines[3].number == 4
        listing.lines[3].text == "}"
        listing.lines[3].lineChunks != null
        listing.lines[3].lineChunks.size() == 1
        listing.lines[3].lineChunks[0].text == "}"
    }

    def "one mark in listing" () {
        given:
        String input = '''\
public class Item {
    private int id;
    private String name;
}
'''

        List<HighlightParameter> params = [
                HighlightParameter.mark(
                  CodePoint.ofPoint(2, 13),
                  CodePoint.ofPoint(2, 15)
                )
        ]

        when:
        Listing listing = processor.process(input.chars, false, null, false, false, params)

        then:
        listing != null
        listing.lines.size() == 4

        listing.lines[0].number == 1
        listing.lines[0].text == "public class Item {"
        listing.lines[0].lineChunks != null
        listing.lines[0].lineChunks.size() == 1
        listing.lines[0].lineChunks[0].text == "public class Item {"

        listing.lines[1].number == 2
        listing.lines[1].text == "    private int id;"
        listing.lines[1].lineChunks != null
        listing.lines[1].lineChunks.size() == 3
        listing.lines[1].lineChunks[0].text == "    private "
        listing.lines[1].lineChunks[1].text == "int"
        listing.lines[1].lineChunks[2].text == " id;"

        listing.lines[2].number == 3
        listing.lines[2].text == "    private String name;"
        listing.lines[2].lineChunks != null
        listing.lines[2].lineChunks.size() == 1
        listing.lines[2].lineChunks[0].text == "    private String name;"

        listing.lines[3].number == 4
        listing.lines[3].text == "}"
        listing.lines[3].lineChunks != null
        listing.lines[3].lineChunks.size() == 1
        listing.lines[3].lineChunks[0].text == "}"
    }

    def "html code" () {
        given:
        String input = '''\
<div>Some code</div>
'''

        when:
        Listing listing = processor.process(input.chars, false, null, false, false, null)

        then:
        listing != null
        listing.lines.size() == 1

        listing.lines[0].number == 1
        listing.lines[0].text == "<div>Some code</div>"
        listing.lines[0].lineChunks != null
        listing.lines[0].lineChunks.size() == 1
        listing.lines[0].lineChunks[0].text == "&lt;div&gt;Some code&lt;/div&gt;"
    }

    def "one mark in html code" () {
        given:
        String input = '''\
<div>Some code</div>
'''

        List<HighlightParameter> params = [
                HighlightParameter.mark(
                        CodePoint.ofPoint(1, 6),
                        CodePoint.ofPoint(1, 14)
                )
        ]

        when:
        Listing listing = processor.process(input.chars, false, null, false, false, params)

        then:
        listing != null
        listing.lines.size() == 1

        listing.lines[0].number == 1
        listing.lines[0].text == "<div>Some code</div>"
        listing.lines[0].lineChunks != null
        listing.lines[0].lineChunks.size() == 3
        listing.lines[0].lineChunks[0].text == "&lt;div&gt;"
        listing.lines[0].lineChunks[1].text == "Some code"
        listing.lines[0].lineChunks[2].text == "&lt;/div&gt;"
    }

    def "should have two callouts on second line" () {
        given:
        String input = '''\
public class Item {
    private int id; <1> <2>
    private String name;
}
'''

        when:
        Listing listing = processor.process(input.chars, false, null, false, false, null)

        then:
        listing != null
        listing.lines != null
        listing.lines.size() == 4

        listing.lines[0].callouts == null

        println(listing.lines[1].text)
        println(listing.lines[1].callouts)

        listing.lines[1].callouts != null
        listing.lines[1].callouts.size() == 2

        listing.lines[1].callouts[0].line == 2
        listing.lines[1].callouts[0].num == 1

        listing.lines[1].callouts[1].line == 2
        listing.lines[1].callouts[1].num == 2

        listing.lines[2].callouts == null
        listing.lines[3].callouts == null
    }

}