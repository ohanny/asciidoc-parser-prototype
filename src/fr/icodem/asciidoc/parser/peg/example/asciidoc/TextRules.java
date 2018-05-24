package fr.icodem.asciidoc.parser.peg.example.asciidoc;

import fr.icodem.asciidoc.parser.peg.BaseRules;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory;

import java.util.function.Supplier;

public class TextRules extends BaseRules {

    private AttributeEntries attributeEntries;
    private CommonRules commonRules;

    public TextRules(AttributeEntries attributeEntries) {
        this.attributeEntries = attributeEntries;
        this.commonRules = new CommonRules(attributeEntries);
    }

    @Override
    public void withFactory(RulesFactory factory) {
        super.withFactory(factory);
        commonRules.withFactory(factory);
    }

    // imported rules from common
    private Rule attributeList() {
        return commonRules.attributeList(true);
    }

    private Rule macro() {
        return commonRules.macro(true);
    }

    // formatted text rules
    public Rule formattedText() {
        return node("formattedText",
                 zeroOrMore(chunk())
               )
        ;
    }

    private Rule chunk() {
        return named("chunk",
                 oneOrMore(
                   firstOf(
                     attListAndMark(),
                     macro(),
                     xref(),
                     bold(),
                     italic(),
                     subscript(),
                     superscript(),
                     monospace(),
                     text()
                   )
                 )
               )
        ;
    }

    private Rule chunkold() {
        return named("chunk",
                 oneOrMore(
                   firstOf(
                     attListAndMark(),
                     macro(),
                     xref(),
                     text(),
                     bold(),
                     italic(),
                     subscript(),
                     superscript(),
                     monospace()
                   )
                 )
               )
        ;
    }

    private Rule text() {
        return node("text",
                 oneOrMore(
                   firstOf(
                     openingSingleQuote(),
                     closingSingleQuote(),
                     openingDoubleQuote(),
                     closingDoubleQuote(),
                     string("\\*"),
                     string("\\_"),
                     string("\\`"),
                     string("\\#"),
                     sequence(
                       test(ch('*')),
                       notInsideBold(),
                       testNot(bold()),
                       ch('*')
                     ),
                     sequence(
                       test(ch('_')),
                       notInsideItalic(),
                       testNot(italic()),
                       ch('_')
                     ),
                     sequence(
                       test(ch('`')),
                       notInsideMonospace(),
                       testNot(monospace()),
                       ch('`')
                     ),
                     sequence(
                       test(ch('~')),
                       notInsideSubscript(),
                       testNot(subscript()),
                       ch('~')
                     ),
                     sequence(
                       test(ch('^')),
                       notInsideSuperscript(),
                       testNot(superscript()),
                       ch('^')
                     ),
                     sequence(
                       test(ch('#')),
                       notInsideMark(),
                       testNot(mark()),
                       ch('#')
                     ),
                     sequence(
                       test(ch('[')),
                       testNot(attListAndMark()),
                       ch('[')
                     ),
                     sequence(
                       markPositionInParent(),
                       string("image:"),
                       firstOf(
                         hasBeenCheckedAsNotAMacro(),
                         breakTextRequest()
                       )
                     ),
                     sequence(
                       test(ch('<')),
                       testNot(xref()),
                       ch('<')
                     ),
                     sequence(
                       textShouldNotBreak(),
                       noneOf("*_~^`#[<")
                     )
                   )
                 )
               )
        ;
    }

    private Rule textold() {
        return node("text",
                 oneOrMore(
                   firstOf(
                     string("\\*"),
                     string("\\_"),
                     string("\\`"),
                     string("\\#"),
                     sequence(
                       test(ch('[')),
                       testNot(attListAndMark()),
                       ch('[')
                     ),
                     openingSingleQuote(),
                     closingSingleQuote(),
                     openingDoubleQuote(),
                     closingDoubleQuote(),
                     sequence(
                       markPositionInParent(),
                       string("image:"),
                       firstOf(
                         hasBeenCheckedAsNotAMacro(),
                         breakTextRequest()
                       )
                     ),
                     sequence(
                       test(ch('<')),
                       testNot(xref()),
                       ch('<')
                     ),
                     sequence(
                       textShouldNotBreak(),
                       noneOf("*_~^`#[<")
                     )
                   )
                 )
               )
        ;
    }

    private Rule markPositionInParent() {
        return () -> ctx -> {
            ctx.getParent().setAttribute("position", ctx.getPosition());
            return true;
        };
    }

    private Rule hasBeenCheckedAsNotAMacro() {
        return () -> ctx -> {
            int position = ctx.getIntAttribute("position", -1);
            boolean result = ctx.getBooleanAttribute(position + ".not-a-macro", false);
            return result;
        };
    }

    private Rule breakTextRequest() {
        return () -> ctx -> {
            ctx.findParentContextNode().setAttribute("break", true);
            return false;
        };
    }

    private Rule textShouldNotBreak() {
        return () -> ctx -> !ctx.getBooleanAttribute("break", false);
    }

    private Rule notInsideBold() {
        return () -> ctx -> !ctx.getRoot().getBooleanAttribute("insideBold", false);
    }

    private Rule bold() {
        Rule setInsideBold = () -> ctx -> {
            ctx.getRoot().setAttribute("insideBold", true);
            return true;
        };

        Rule unsetInsideBold = () -> ctx -> {
            ctx.getRoot().setAttribute("insideBold", false);
            return true;
        };

        return node("bold",
                 sequence(
                   test(ch('*')),
                   notInsideBold(),
                   firstOf(
                     sequence(
                       oneOrMore(ch('*')),
                       setInsideBold,
                       oneOrMore(proxy("chunk")),
                       oneOrMore(ch('*')),
                       unsetInsideBold
                     ),
                     testNot(unsetInsideBold)
                   )
                 )
               )
        ;
    }

    private Rule notInsideItalic() {
        return () -> ctx -> !ctx.getRoot().getBooleanAttribute("insideItalic", false);
    }

    private Rule italic() {
        Rule setInsideItalic = () -> ctx -> {
            ctx.getRoot().setAttribute("insideItalic", true);
            return true;
        };

        Rule unsetInsideItalic = () -> ctx -> {
            ctx.getRoot().setAttribute("insideItalic", false);
            return true;
        };


        return node("italic",
                 sequence(
                   test(ch('_')),
                   notInsideItalic(),
                   firstOf(
                     sequence(
                       oneOrMore(ch('_')),
                       setInsideItalic,
                       oneOrMore(proxy("chunk")),
                       oneOrMore(ch('_')),
                       unsetInsideItalic
                     ),
                     testNot(unsetInsideItalic)
                   )
                 )
               )
        ;
    }

    private Rule notInsideSubscript() {
        return () -> ctx -> !ctx.getRoot().getBooleanAttribute("insideSubscript", false);
    }

    private Rule subscript() {
        Rule setInsideSubscript = () -> ctx -> {
            ctx.getRoot().setAttribute("insideSubscript", true);
            return true;
        };

        Rule unsetInsideSubscript = () -> ctx -> {
            ctx.getRoot().setAttribute("insideSubscript", false);
            return true;
        };

        return node("subscript",
                 sequence(
                   test(ch('~')),
                   notInsideSubscript(),
                   firstOf(
                     sequence(
                       oneOrMore(ch('~')),
                       setInsideSubscript,
                       oneOrMore(proxy("chunk")),
                       oneOrMore(ch('~')),
                       unsetInsideSubscript
                     ),
                     testNot(unsetInsideSubscript)
                   )
                 )
               )
        ;
    }

    private Rule notInsideSuperscript() {
        return () -> ctx -> !ctx.getRoot().getBooleanAttribute("insideSuperscript", false);
    }

    private Rule superscript() {
        Rule setInsideSuperscript = () -> ctx -> {
            ctx.getRoot().setAttribute("insideSuperscript", true);
            return true;
        };

        Rule unsetInsideSuperscript = () -> ctx -> {
            ctx.getRoot().setAttribute("insideSuperscript", false);
            return true;
        };

        return node("superscript",
                 sequence(
                   test(ch('^')),
                   notInsideSuperscript(),
                   firstOf(
                     sequence(
                       oneOrMore(ch('^')),
                       setInsideSuperscript,
                       oneOrMore(proxy("chunk")),
                       oneOrMore(ch('^')),
                       unsetInsideSuperscript
                     ),
                     testNot(unsetInsideSuperscript)
                   )
                 )
               )
        ;
    }

    private Rule notInsideMonospace() {
        return () -> ctx -> !ctx.getRoot().getBooleanAttribute("insideMonospace", false);
    }

    private Rule monospace() {
        Rule toggleInsideMonospace = () -> ctx -> {
            boolean b = ctx.getRoot().getBooleanAttribute("insideMonospace", false);
            ctx.getRoot().setAttribute("insideMonospace", !b);
            return true;
        };

        return node("monospace",
                 sequence(
                   test(ch('`')),
                   notInsideMonospace(),
                   testNot(string("`\"")),
                   oneOrMore(ch('`')),
                   toggleInsideMonospace,
                   oneOrMore(proxy("chunk")),
                   zeroOrMore(
                     sequence(
                       testNot(string("`\"")),
                       ch('`')
                     )
                   ),
                   toggleInsideMonospace
                 )
               )
        ;
    }

    private Rule traceChar(String message, boolean match) {
        return () -> ctx -> {
            ctx.mark();
            System.out.println(message + " => " + ctx.getNextChar());
            ctx.reset();
            return match;
        };
    }

    private Rule notInsideMark() {
        return () -> ctx -> !ctx.getRoot().getBooleanAttribute("insideMark", false);
    }

    private Rule mark() {
        Rule setInsideMark = () -> ctx -> {
            ctx.getRoot().setAttribute("insideMark", true);
            return true;
        };

        Rule unsetInsideMark = () -> ctx -> {
            ctx.getRoot().setAttribute("insideMark", false);
            return true;
        };

        return node("mark",
                 sequence(
                   test(ch('#')),
                   notInsideMark(),
                   firstOf(
                     sequence(
                       oneOrMore(ch('#')),
                       setInsideMark,
                       oneOrMore(proxy("chunk")),
                       oneOrMore(ch('#')),
                       unsetInsideMark
                     ),
                     testNot(unsetInsideMark)
                   )
                 )
               )
        ;
    }

    private Rule attListAndMark() {
        return sequence(
                 optional(attributeList()),
                 mark()
               )
        ;
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

    private Rule xref() {
        return node("xref",
                 sequence(
                   times('<', 2),
                   xrefValue(),
                   optional(
                     sequence(
                       ch(','),
                       xrefLabel()
                     )
                   ),
                   times('>', 2)
                 )
               )
        ;
    }

    private Rule xrefValue() {
        return node("xrefValue",
                 oneOrMore(noneOf(",<>\r\n"))
               )
        ;
    }

    private Rule xrefLabel() {
        return node("xrefLabel",
                 oneOrMore(noneOf(",<>\r\n"))
               )
        ;
    }
}
