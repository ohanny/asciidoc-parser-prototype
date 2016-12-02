package fr.icodem.asciidoc.renderer

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.TextOutputter
import spock.lang.Specification

class TextOutputterSpec extends Specification {

    def "append abc"() {
        given:
        StringWriter writer = new StringWriter()
        TextOutputter outputter = new TextOutputter(writer)

        when:
        outputter.append("abc")

        then:
        writer.toString() == "abc"
    }

    def "append abc and new line"() {
        given:
        StringWriter writer = new StringWriter()
        TextOutputter outputter = new TextOutputter(writer)

        when:
        outputter.append("abc")
        outputter.nl()

        then:
        writer.toString() == "abc\r\n"
    }

    def "indent level 1 on second line"() {
        given:
        StringWriter writer = new StringWriter()
        TextOutputter outputter = new TextOutputter(writer)

        when:
        outputter.append("abc")
        outputter.nl()
        outputter.incIndent()
        outputter.indent()
        outputter.append("def")

        then:
        writer.toString() == "abc\r\n  def"
    }

    def "indent level 1 on second line and level 2 on third line"() {
        given:
        StringWriter writer = new StringWriter()
        TextOutputter outputter = new TextOutputter(writer)

        when:
        outputter.append("abc")
        outputter.nl()
        outputter.incIndent()
        outputter.indent()
        outputter.append("def")
        outputter.nl()
        outputter.incIndent()
        outputter.indent()
        outputter.append("ghi")

        then:
        writer.toString() == "abc\r\n  def\r\n    ghi"
    }

    def "data after buffer on are not outputted if buffer off is not requested"() {
        given:
        StringWriter writer = new StringWriter();
        TextOutputter outputter = new TextOutputter(writer)

        when:
        outputter.append("abc")
        outputter.bufferOn()
        outputter.append("def")

        then:
        writer.toString() == "abc"
    }

    def "data after buffer on are outputted if buffer off is requested"() {
        given:
        StringWriter writer = new StringWriter();
        TextOutputter outputter = new TextOutputter(writer)

        when:
        outputter.append("abc")
        outputter.bufferOn()
        outputter.append("def")
        outputter.bufferOff()

        then:
        writer.toString() == "abcdef"
    }

    def "insert data using a mark"() {
        given:
        StringWriter writer = new StringWriter()
        TextOutputter outputter = new TextOutputter(writer)

        when:
        outputter.bufferOn()
        outputter.append("abc")
        outputter.mark("1")
        outputter.append("def")
        outputter.moveTo("1")
        outputter.append("ghi")
        outputter.bufferOff()

        then:
        writer.toString() == "abcghidef"
    }

    def "insert data using a mark, then append data at the end"() {
        given:
        StringWriter writer = new StringWriter()
        TextOutputter outputter = new TextOutputter(writer)

        when:
        outputter.bufferOn()
        outputter.append("abc")
        outputter.mark("1")
        outputter.append("def")
        outputter.moveTo("1")
        outputter.append("ghi")
        outputter.moveEnd()
        outputter.append("jkl")
        outputter.bufferOff()

        then:
        writer.toString() == "abcghidefjkl"
    }

    def "insert data using two marks"() {
        given:
        StringWriter writer = new StringWriter()
        TextOutputter outputter = new TextOutputter(writer)

        when:
        outputter.bufferOn()
        outputter.append("abc")
        outputter.mark("1")
        outputter.append("def")
        outputter.moveTo("1")
        outputter.append("ghi")
        outputter.mark("2")
        outputter.append("jkl")
        outputter.moveTo("2")
        outputter.append("mno")
        outputter.bufferOff()

        then:
        writer.toString() == "abcghimnojkldef"
    }

    def "two marks before move"() {
        given:
        StringWriter writer = new StringWriter()
        TextOutputter outputter = new TextOutputter(writer)

        when:
        outputter.bufferOn()
        outputter.append("abc")
        outputter.mark("1")
        outputter.append("def")
        outputter.mark("2")
        outputter.append("ghi")
        outputter.moveTo("1")
        outputter.append("jkl")
        outputter.moveTo("2")
        outputter.append("mno")
        outputter.bufferOff()

        then:
        writer.toString() == "abcjkldefmnoghi"
    }

    def "interwoven marks (important for nested lists)"() {
        given:
        StringWriter writer = new StringWriter()
        TextOutputter outputter = new TextOutputter(writer)

        when:
        outputter.bufferOn()
        outputter.append("ab")
        outputter.mark("1")
        outputter.append("cd")
        outputter.mark("2")
        outputter.moveTo("1")
        outputter.append("ef")
        outputter.mark("3")
        outputter.append("gh")
        outputter.mark("4")
        outputter.moveTo("3")
        outputter.append("ij")
        outputter.mark("5")
        outputter.append("kl")
        outputter.mark("6")
        outputter.moveTo("4")
        outputter.append("mn")
        outputter.moveTo("2")
        outputter.append("op")
        outputter.bufferOff()

        then:
        writer.toString() == "abefijklghmncdop"
    }

}