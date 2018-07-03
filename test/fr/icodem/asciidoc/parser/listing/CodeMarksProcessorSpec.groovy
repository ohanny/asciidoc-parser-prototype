package fr.icodem.asciidoc.parser.listing

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.CodeMarksProcessor
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.CodePoint
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.HighlightParameter
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.LineChunkContext
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.LineContext
import spock.lang.Ignore
import spock.lang.Specification

class CodeMarksProcessorSpec extends Specification {
    def processor = new CodeMarksProcessor()

    def "no mark" () {
        given:
        String input = '''\
let str = "hello";
'''

        List<LineContext> lines = [
            LineContext.of(1, input.chars, 0, 18)
        ]

        when:
        processor.process(lines, null)

        then:
        lines[0].chunks != null
        lines[0].chunks.size() == 1

        LineChunkContext chunk = lines[0].chunks[0]
        chunk.text == "let str = \"hello\";"
        chunk.not == false
        chunk.important == false
        chunk.comment == false
        chunk.mark == false
        chunk.highlight == false

    }

    def "one mark" () {
        given:
        String input = '''\
let str = "hello";
'''

        List<LineContext> lines = [
            LineContext.of(1, input.chars, 0, 18)
        ]

        List<HighlightParameter> params = [
            HighlightParameter.mark(CodePoint.ofPoint(1, 5), CodePoint.ofPoint(1, 7), 0)
        ]

        when:
        processor.process(lines, params)

        then:
        lines[0].chunks != null
        lines[0].chunks.size() == 3

        LineChunkContext chunk0 = lines[0].chunks[0]
        chunk0.text == "let "
        chunk0.not == false
        chunk0.important == false
        chunk0.comment == false
        chunk0.mark == false
        chunk0.highlight == false

        LineChunkContext chunk1 = lines[0].chunks[1]
        chunk1.text == "str"
        chunk1.important == false
        chunk1.comment == false
        chunk1.mark == true
        chunk1.highlight == false

        LineChunkContext chunk2 = lines[0].chunks[2]
        chunk2.text == " = \"hello\";"
        chunk2.not == false
        chunk2.important == false
        chunk2.comment == false
        chunk2.mark == false
        chunk2.highlight == false

    }

    def "two marks in same line" () {
        given:
        String input = '''\
let str = "hello";
'''

        List<LineContext> lines = [
            LineContext.of(1, input.chars, 0, 18)
        ]

        List<HighlightParameter> params = [
            HighlightParameter.mark(CodePoint.ofPoint(1, 5), CodePoint.ofPoint(1, 7), 0),
            HighlightParameter.mark(CodePoint.ofPoint(1, 12), CodePoint.ofPoint(1, 16), 0)
        ]

        when:
        processor.process(lines, params)

        then:
        lines[0].chunks != null
        lines[0].chunks.size() == 5

        LineChunkContext chunk0 = lines[0].chunks[0]
        chunk0.text == "let "
        chunk0.not == false
        chunk0.important == false
        chunk0.comment == false
        chunk0.mark == false
        chunk0.highlight == false

        LineChunkContext chunk1 = lines[0].chunks[1]
        chunk1.text == "str"
        chunk1.important == false
        chunk1.comment == false
        chunk1.mark == true
        chunk1.highlight == false

        LineChunkContext chunk2 = lines[0].chunks[2]
        chunk2.text == " = \""
        chunk2.not == false
        chunk2.important == false
        chunk2.comment == false
        chunk2.mark == false
        chunk2.highlight == false

        LineChunkContext chunk3 = lines[0].chunks[3]
        chunk3.text == "hello"
        chunk3.not == false
        chunk3.important == false
        chunk3.comment == false
        chunk3.mark == true
        chunk3.highlight == false

        LineChunkContext chunk4 = lines[0].chunks[4]
        chunk4.text == "\";"
        chunk4.not == false
        chunk4.important == false
        chunk4.comment == false
        chunk4.mark == false
        chunk4.highlight == false

    }

    def "two lines, no mark" () {
        given:
        String input = '''\
let str = "hello";
console.log(str);
'''

        List<LineContext> lines = [
            LineContext.of(1, input.chars, 0, 18),
            LineContext.of(2, input.chars, 19, 17)
        ]
        List<HighlightParameter> params = null

        when:
        processor.process(lines, params)

        then:
        lines[0].chunks != null
        lines[0].chunks.size() == 1

        LineChunkContext chunk0 = lines[0].chunks[0]
        chunk0.text == "let str = \"hello\";"
        chunk0.not == false
        chunk0.important == false
        chunk0.comment == false
        chunk0.mark == false
        chunk0.highlight == false

        lines[1].chunks != null
        lines[1].chunks.size() == 1

        LineChunkContext chunk1 = lines[1].chunks[0]
        chunk1.text == "console.log(str);"
        chunk1.not == false
        chunk1.important == false
        chunk1.comment == false
        chunk1.mark == false
        chunk1.highlight == false

    }

    def "mark entire line" () {
        given:
        String input = '''\
let str = "hello";
'''

        List<LineContext> lines = [
            LineContext.of(1, input.chars, 0, 18)
        ]

        List<HighlightParameter> params = [
            HighlightParameter.mark(CodePoint.ofLine(1), CodePoint.ofLine(1), 0)
        ]

        when:
        processor.process(lines, params)

        then:
        lines[0].chunks != null
        lines[0].chunks.size() == 1

        LineChunkContext chunk0 = lines[0].chunks[0]
        chunk0.text == "let str = \"hello\";"
        chunk0.not == false
        chunk0.important == false
        chunk0.comment == false
        chunk0.mark == true
        chunk0.highlight == false
    }

    def "one nested mark" () {
        given:
        String input = '''\
let str = "hello";
'''

        List<LineContext> lines = [
            LineContext.of(1, input.chars, 0, 18)
        ]

        List<HighlightParameter> params = [
            HighlightParameter.mark(CodePoint.ofPoint(1, 1), CodePoint.ofPoint(1, 17), 0),
            HighlightParameter.strong(CodePoint.ofPoint(1, 5), CodePoint.ofPoint(1, 7))
        ]

        when:
        processor.process(lines, params)

        then:
        lines[0].chunks != null
        lines[0].chunks.size() == 2

        LineChunkContext chunk0 = lines[0].chunks[0]
        chunk0.text == "let str = \"hello\""
        chunk0.not == false
        chunk0.important == false
        chunk0.comment == false
        chunk0.mark == true
        chunk0.highlight == false
        chunk0.strong == false

        chunk0.chunks != null
        chunk0.chunks.size() == 3

        LineChunkContext chunk0_0 = chunk0.chunks[0]
        chunk0_0.text == "let "
        chunk0_0.not == false
        chunk0_0.important == false
        chunk0_0.comment == false
        chunk0_0.mark == false
        chunk0_0.highlight == false
        chunk0_0.strong == false

        LineChunkContext chunk0_1 = chunk0.chunks[1]
        chunk0_1.text == "str"
        chunk0_1.not == false
        chunk0_1.important == false
        chunk0_1.comment == false
        chunk0_1.mark == false
        chunk0_1.highlight == false
        chunk0_1.strong == true

        LineChunkContext chunk0_2 = chunk0.chunks[2]
        chunk0_2.text == " = \"hello\""
        chunk0_2.not == false
        chunk0_2.important == false
        chunk0_2.comment == false
        chunk0_2.mark == false
        chunk0_2.highlight == false
        chunk0_2.strong == false

        LineChunkContext chunk1 = lines[0].chunks[1]
        chunk1.text == ";"
        chunk1.not == false
        chunk1.important == false
        chunk1.comment == false
        chunk1.mark == false
        chunk1.highlight == false
        chunk1.strong == false
    }

}