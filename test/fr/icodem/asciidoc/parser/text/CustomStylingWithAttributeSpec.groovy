package fr.icodem.asciidoc.parser.text

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class CustomStylingWithAttributeSpec extends TextSpecification {

    def "marked phrase with custom css class"() {
        given:
        String input = "[yellow]#It's a nice day#";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (mark (attributeList [ (positionalAttribute (attributeValue y e l l o w)) ]) # (text I t ' s   a   n i c e   d a y) #))";
    }

    def "marked phrase with two custom css classes"() {
        given:
        String input = "[yellow shiny]#It's a nice day#";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (mark (attributeList [ (positionalAttribute (attributeValue y e l l o w   s h i n y)) ]) # (text I t ' s   a   n i c e   d a y) #))";
    }

    def "marked word within phrase with custom css class"() {
        given:
        String input = "It's a [yellow]#nice# day";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text I t ' s   a  ) (mark (attributeList [ (positionalAttribute (attributeValue y e l l o w)) ]) # (text n i c e) #) (text   d a y))";
    }

    def "marked word within phrase with two custom css classes"() {
        given:
        String input = "It's a [yellow shiny]#nice# day";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text I t ' s   a  ) (mark (attributeList [ (positionalAttribute (attributeValue y e l l o w   s h i n y)) ]) # (text n i c e) #) (text   d a y))";
    }


}