package fr.icodem.asciidoc.parser.listing

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.LineContext
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.SplitLinesProcessor
import spock.lang.Specification

class SplitLinesProcessorSpec extends Specification {
    def processor = new SplitLinesProcessor()

    def "should extract lines" () {
        given:
        String input = '''\
let str = "hello";
console.log(str);
'''

        when:
        List<LineContext> lines = processor.process(input.chars)

        then:
        lines != null
        lines.size() == 2

        lines[0].offset == 0
        lines[0].length == 18
        lines[0].data != null
        lines[0].data.toString() == input

        lines[1].offset == 19
        lines[1].length == 17
        lines[1].data != null
        lines[1].data.toString() == input

    }

    def "lines separated with \r\n" () {
        given:
        String input = 'let str = "hello";\r\nconsole.log(str);\r\n'

        when:
        List<LineContext> lines = processor.process(input.chars)

        then:
        lines != null
        lines.size() == 2

        lines[0].offset == 0
        lines[0].length == 18
        lines[0].data != null
        lines[0].data.toString() == input

        lines[1].offset == 20
        lines[1].length == 17
        lines[1].data != null
        lines[1].data.toString() == input

    }

    def "listing with blank line" () {
        given:
        String input = '''\
int id = 1;

System.out.println(id);
'''

        when:
        List<LineContext> lines = processor.process(input.chars)

        then:
        lines != null
        lines.size() == 3

        lines[0].offset == 0
        lines[0].length == 11
        lines[0].data != null
        lines[0].data.toString() == input

        lines[1].offset == 12
        lines[1].length == 0
        lines[1].data != null
        lines[1].data.toString() == input

        lines[2].offset == 13
        lines[2].length == 23
        lines[2].data != null
        lines[2].data.toString() == input

    }

}