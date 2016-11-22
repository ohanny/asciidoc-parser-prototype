package fr.icodem.asciidoc.parser.backend

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.TextOutputter
import spock.lang.Specification


class TextOutputterSpec extends Specification {

    def "append abc"() {
        given:
        StringWriter writer = new StringWriter();
        TextOutputter outputter = new TextOutputter(writer);

        when:
        outputter.append("abc");

        then:
        writer.toString() == "abc";
    }

    def "append abc and new line"() {
        given:
        StringWriter writer = new StringWriter();
        TextOutputter outputter = new TextOutputter(writer);

        when:
        outputter.append("abc");
        outputter.nl();

        then:
        writer.toString() == "abc\r\n";
    }

    def "indent once after new line"() {
        given:
        StringWriter writer = new StringWriter();
        TextOutputter outputter = new TextOutputter(writer);

        when:
        outputter.append("abc");
        outputter.nl();
        outputter.incrementIndentLevel();
        outputter.indent();
        outputter.append("cde")

        then:
        writer.toString() == "abc\r\n  cde";
    }

    def "indent twice after new line"() {
        given:
        StringWriter writer = new StringWriter();
        TextOutputter outputter = new TextOutputter(writer);

        when:
        outputter.append("abc");
        outputter.nl();
        outputter.incrementIndentLevel();
        outputter.indent();
        outputter.indent();
        outputter.append("cde")

        then:
        writer.toString() == "abc\r\n    cde";
    }

}