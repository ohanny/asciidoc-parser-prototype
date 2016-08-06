package fr.icodem.asciidoc.parser.peg.example;

import fr.icodem.asciidoc.parser.peg.BaseParser;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

public class FormattedTextParser extends BaseParser  {
    public Rule formattedText() {
        return node("formattedText",
                zeroOrMore(chunk())
        );
    }

    private Rule chunk() {
        return named("chunk",
                oneOrMore(firstOf(
                    text(),
                    bold(),
                    italic()
                )));
    }

    private Rule text() {
        return node("text",
                oneOrMore(firstOf(
                        string("\\*"),
                        string("\\_"),
                        noneOf("*_")
                )));
    }

    private Rule bold() {
        return node("bold",
                sequence(
                    ch('*'),
                    oneOrMore(proxy("chunk")),
                    ch('*')
                ));
    }

    private Rule italic() {
        return node("italic",
                sequence(
                    ch('_'),
                    oneOrMore(proxy("chunk")),
                    ch('_')
                ));
    }

}
