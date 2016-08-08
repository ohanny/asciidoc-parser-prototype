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

        /* strict rule
        return node("bold",
                sequence(
                        notInsideBold,
                        ch('*'),
                        toggleInsideBold,
                        oneOrMore(proxy("chunk")),
                        ch('*'),
                        toggleInsideBold
                ));
                */

        // permissive rule
        Rule toggleInsideBold = () -> ctx -> {
            boolean b = ctx.getParent().getBooleanAttribute("insideBold");
            ctx.getParent().setAttribute("insideBold", !b);
            return true;
        };

        Rule notInsideBold = () -> ctx -> !ctx.getParent().getBooleanAttribute("insideBold");

        return node("bold",
                sequence(
                    notInsideBold,
                    oneOrMore(ch('*')),
                    toggleInsideBold,
                    oneOrMore(proxy("chunk")),
                    zeroOrMore(ch('*')),
                    toggleInsideBold
                ));
    }

    private Rule italic() {
        /* strict rule
        return node("italic",
                sequence(
                    ch('_'),
                    oneOrMore(proxy("chunk")),
                    ch('_')
                ));
        */

        // permissive rule
        Rule toggleInsideItalic = () -> ctx -> {
            boolean b = ctx.getParent().getBooleanAttribute("insideItalic");
            ctx.getParent().setAttribute("insideItalic", !b);
            return true;
        };

        Rule notInsideItalic = () -> ctx -> !ctx.getParent().getBooleanAttribute("insideItalic");

        return node("italic",
                sequence(
                        notInsideItalic,
                        oneOrMore(ch('_')),
                        toggleInsideItalic,
                        oneOrMore(proxy("chunk")),
                        zeroOrMore(ch('_')),
                        toggleInsideItalic
                ));
    }

    private Rule subscript() {
        /* strict rule
        return node("subscript",
                sequence(
                    ch('~'),
                    oneOrMore(proxy("chunk")),
                    optional(ch('~'))
                ));
                */

        // permissive rule
        Rule toggleInsideSubscript = () -> ctx -> {
            boolean b = ctx.getParent().getBooleanAttribute("insideSubscript");
            ctx.getParent().setAttribute("insideSubscript", !b);
            return true;
        };

        Rule notInsideSubscript = () -> ctx -> !ctx.getParent().getBooleanAttribute("insideSubscript");

        return node("subscript",
                sequence(
                        notInsideSubscript,
                        oneOrMore(ch('~')),
                        toggleInsideSubscript,
                        oneOrMore(proxy("chunk")),
                        zeroOrMore(ch('~')),
                        toggleInsideSubscript
                ));

    }

    private Rule superscript() {
        /* strict rule
        return node("superscript",
                sequence(
                    ch('^'),
                    oneOrMore(proxy("chunk")),
                    optional(ch('^'))
                ));
                */

        // permissive rule
        Rule toggleInsideSuperscript = () -> ctx -> {
            boolean b = ctx.getParent().getBooleanAttribute("insideSuperscript");
            ctx.getParent().setAttribute("insideSuperscript", !b);
            return true;
        };

        Rule notInsideSuperscript = () -> ctx -> !ctx.getParent().getBooleanAttribute("insideSuperscript");

        return node("superscript",
                sequence(
                        notInsideSuperscript,
                        oneOrMore(ch('^')),
                        toggleInsideSuperscript,
                        oneOrMore(proxy("chunk")),
                        zeroOrMore(ch('^')),
                        toggleInsideSuperscript
                ));

    }

    private Rule monospace() {
        /* strict rule
        return node("monospace",
                sequence(
                    ch('`'),
                    oneOrMore(proxy("chunk")),
                    ch('`')
                ));
                */

        // permissive rule
        Rule toggleInsideMonospace = () -> ctx -> {
            boolean b = ctx.getParent().getBooleanAttribute("insideMonospace");
            ctx.getParent().setAttribute("insideMonospace", !b);
            return true;
        };

        Rule notInsideMonospace = () -> ctx -> !ctx.getParent().getBooleanAttribute("insideMonospace");

        return node("monospace",
                sequence(
                        notInsideMonospace,
                        oneOrMore(ch('`')),
                        toggleInsideMonospace,
                        oneOrMore(proxy("chunk")),
                        zeroOrMore(sequence(testNot(string("`\"")), ch('`'))),
                        toggleInsideMonospace
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
