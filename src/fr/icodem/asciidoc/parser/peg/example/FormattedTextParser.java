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
                    italic(),
                    subscript(),
                    superscript(),
                    monospace()
                )));
    }

    private Rule text() {
        return node("text",
                oneOrMore(firstOf(
                        string("\\*"),
                        string("\\_"),
                        string("\\`"),
                        openingSingleQuote(),
                        closingSingleQuote(),
                        openingDoubleQuote(),
                        closingDoubleQuote(),
                        noneOf("*_~^`")
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

    private Rule subscript() {
        return node("subscript",
                sequence(
                    ch('~'),
                    oneOrMore(proxy("chunk")),
                    ch('~')
                ));
    }

    private Rule superscript() {
        return node("superscript",
                sequence(
                    ch('^'),
                    oneOrMore(proxy("chunk")),
                    ch('^')
                ));
    }

    private Rule monospace() {
        return node("monospace",
                sequence(
                    ch('`'),
                    oneOrMore(proxy("chunk")),
                    ch('`')
                ));
    }

    private Rule openingSingleQuote() {
        return node("openingSingleQuote", string("'`"));
    }

    private Rule closingSingleQuote() {
        return node("closingSingleQuote", string("`'"));
    }

    private Rule openingDoubleQuote() {
        return node("openingDoubleQuote", string("\"`"));
    }

    private Rule closingDoubleQuote() {
        return node("closingDoubleQuote", string("`\""));
    }

}
