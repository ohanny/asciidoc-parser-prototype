package fr.icodem.asciidoc.parser.listing

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.CalloutProcessor
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.LineContext
import spock.lang.Specification

class CalloutProcessorSpec extends Specification {
    def processor = new CalloutProcessor()

    def "should extract one callout" () {
        given:
        String input = 'let str = "hello"; <1>'
        LineContext context = LineContext.of(1, input.chars, 0, input.length())

        when:
        processor.processCallouts(context)

        then:
        context.callouts != null
        context.callouts.size() == 1

        context.callouts[0].line == 1
        context.callouts[0].num == 1

        context.length == input.length() - 3

    }

    def "should extract two callouts" () {
        given:
        String input = 'let str = "hello"; <1> <2>'
        LineContext context = LineContext.of(1, input.chars, 0, input.length())

        when:
        processor.processCallouts(context)

        then:
        context.callouts != null
        context.callouts.size() == 2

        context.callouts[0].line == 1
        context.callouts[0].num == 1

        context.callouts[1].line == 1
        context.callouts[1].num == 2

    }

}