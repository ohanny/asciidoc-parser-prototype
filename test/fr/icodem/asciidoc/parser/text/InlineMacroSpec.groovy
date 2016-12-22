package fr.icodem.asciidoc.parser.text

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class InlineMacroSpec extends TextSpecification {

    def "text with image macro"() {
        given:
        String input = "It's a nice image:sunset.png[] day "

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (text I t ' s   a   n i c e  ) (macro (macroName i m a g e) : (macroTarget s u n s e t . p n g) (attributeList [ ]  )) (text d a y  ))"
    }

    def "positional attribute in image macro"() {
        given:
        String input = "It's a nice image:sunset.png[The Sun] day "

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (text I t ' s   a   n i c e  ) (macro (macroName i m a g e) : (macroTarget s u n s e t . p n g) (attributeList [ (positionalAttribute (attributeValue T h e   S u n)) ]  )) (text d a y  ))"
    }

    def "several attributes in image macro"() {
        given:
        String input = "It's a nice image:sunset.jpg[Sunset, 300, 200, link=\"http://www.flickr.com/photos/javh/5448336655\"] day"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (text I t ' s   a   n i c e  ) (macro (macroName i m a g e) : (macroTarget s u n s e t . j p g) (attributeList [ (positionalAttribute (attributeValue S u n s e t)) ,   (positionalAttribute (attributeValue 3 0 0)) ,   (positionalAttribute (attributeValue 2 0 0)) ,   (namedAttribute (attributeName l i n k) = \" (attributeValue h t t p : / / w w w . f l i c k r . c o m / p h o t o s / j a v h / 5 4 4 8 3 3 6 6 5 5) \") ]  )) (text d a y))"
    }


}