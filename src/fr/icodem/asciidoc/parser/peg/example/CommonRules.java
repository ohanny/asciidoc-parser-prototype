package fr.icodem.asciidoc.parser.peg.example;

import fr.icodem.asciidoc.parser.peg.BaseParser;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

public class CommonRules extends BaseParser {

    public Rule macro(boolean inline) {
        if (isCached("macro")) return cached("macro");

        return node("macro",
                sequence(
                    name(),
                    ch(':'), // TODO should accept double and single : ?
                    optional(target()),
                    attributeList(inline)
                )
        );
    }

    private Rule target() {
        if (isCached("target")) return cached("target");

        return node("target", namePrototype());
    }

    public Rule attributeList2() {
        if (isCached("attributeList")) return cached("attributeList");

        return node("attributeList", sequence(
                ch('['),
                firstOf(
                        positionalAttribute()
                ),
                ch(']')
                //firstOf(newLine(), eoi())// TODO replace
        ));
    }


    public Rule attributeList(boolean fromInline) {

        String nameInCache = "attributeList." + (fromInline?"inline":"block");
        if (isCached(nameInCache)) return cached(nameInCache);

        Rule inline = () -> ctx -> {
            return fromInline;
        };

        return node("attributeList", nameInCache, sequence(
                ch('['),
                firstOf(
                        sequence(
                                firstOf(
                                        namedAttribute(),
                                        sequence(positionalAttribute(), optional(idAttribute()), zeroOrMore(firstOf(roleAttribute(), optionAttribute()))),
                                        sequence(idAttribute(), zeroOrMore(firstOf(roleAttribute(), optionAttribute()))),
                                        oneOrMore(firstOf(roleAttribute(), optionAttribute()))

                                ),
                                optional(blank()),// TODO replace,
                                zeroOrMore(sequence(
                                        ch(','),
                                        optional(blank()),// TODO replace,
                                        firstOf(namedAttribute(), positionalAttribute()),
                                        optional(blank())// TODO replace
                                ))
                        ),
                        empty() // TODO replace with optional(sequence) ?
                ),
                ch(']'),
                optional(blank()),// TODO replace
                firstOf(inline, firstOf(newLine(), eoi()))// TODO replace
        ));
    }

    private Rule namedAttribute() {
        if (isCached("namedAttribute")) return cached("namedAttribute");

        return node("namedAttribute",
                sequence(
                    name(),
                    ch('='),
                    attributeValue()
                )
        );
    }

    private Rule positionalAttribute() {
        if (isCached("positionalAttribute")) return cached("positionalAttribute");

        return node("positionalAttribute",
                attributeValue()
        );
    }

    private Rule idAttribute() {
        if (isCached("idAttribute")) return cached("idAttribute");

        return node("idAttribute",
                sequence(
                    ch('#'),
                    name()
                )
        );
    }

    private Rule roleAttribute() {
        if (isCached("roleAttribute")) return cached("roleAttribute");

        return node("roleAttribute",
                sequence(
                    ch('.'),
                    name()
                )
        );
    }

    private Rule optionAttribute() {
        if (isCached("optionAttribute")) return cached("optionAttribute");

        return node("optionAttribute",
                sequence(
                    ch('%'),
                    name()
                )
        );
    }

    private Rule name() {
        if (isCached("name")) return cached("name");

        return node("name", namePrototype());
    }

    private Rule namePrototype() {
        if (isCached("namePrototype")) return cached("namePrototype");

        return cached("namePrototype",
                sequence(
                    optional(blank()),
                    markNodePosition(),
                    oneOrMore(
                        firstOf(
                            sequence(
                                isGreaterThanMarkedNodePosition(),
                                ch('-')
                            ),
                            digitOrLetter()
                        )
                    ),
                    optional(blank())
                )
        );
    }

    private Rule attributeValue() {
        if (isCached("attributeValue")) return cached("attributeValue");

        return node("attributeValue",
                zeroOrMore(
                    noneOf("[],")
                )
        );
    }

    public Rule blank() {
        if (isCached("blank")) return cached("blank");

        return cached("blank",
                oneOrMore(firstOf(' ', '\t'))
        );
    }

    public Rule newLine() {
        if (isCached("newLine")) return cached("newLine");

        return cached("newLine",
                sequence(optional('\r'), ch('\n'))
        );
    }

    private Rule isGreaterThanMarkedNodePosition() {
        if (isCached("isGreaterThanMarkedNodePosition"))
            return cached("isGreaterThanMarkedNodePosition");

        return cached("isGreaterThanMarkedNodePosition",
                () -> ctx -> ctx.getIntAttribute("position", -1) < ctx.getPosition());
    }

    private Rule markNodePosition() {
        if (isCached("markNodePosition")) return cached("markNodePosition");

        return cached("markNodePosition",
                () -> ctx -> {
                    int pos = ctx.getPosition();
                    ctx.findParentContextNode().setAttribute("position", pos);
                    return true;
                });
    }

    public Rule digit() {
        if (isCached("digit")) return cached("digit");

        return cached("digit",
                oneOrMore(charRange('0', '9'))
        );
    }

    public Rule letter() {
        if (isCached("letter")) return cached("letter");

        return cached("letter",
                firstOf(
                    charRange('a', 'z'),
                    charRange('A', 'Z')
                )
        );
    }

    public Rule digitOrLetter() {
        if (isCached("digitOrLetter")) return cached("digitOrLetter");

        return cached("digitOrLetter",
                firstOf(
                    digit(),
                    letter()
                )
        );
    }

}
