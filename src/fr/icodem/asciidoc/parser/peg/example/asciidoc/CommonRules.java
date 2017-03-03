package fr.icodem.asciidoc.parser.peg.example.asciidoc;

import fr.icodem.asciidoc.parser.peg.BaseRules;
import fr.icodem.asciidoc.parser.peg.action.ActionContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.SourceResolver;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

import java.util.function.Supplier;

public class CommonRules extends BaseRules {

    private SourceResolver sourceResolver;
    private AttributeEntries attributeEntries;

    public CommonRules(AttributeEntries attributeEntries) {
        this.attributeEntries = attributeEntries;
        this.sourceResolver = SourceResolver.defaultResolver(attributeEntries);
    }

    public void withSourceResolver(SourceResolver resolver) {
        this.sourceResolver = resolver;
    }

    public Rule macro(boolean fromInline) {
        String nameInCache = "macro." + (fromInline?"inline":"block");
        if (isCached(nameInCache)) return cached(nameInCache);

        return node("macro", nameInCache,
                 action(
                   sequence(
                     firstOf(
                       sequence(
                         markNodePosition(),
                         macroName(),
                         ch(':'), optional(':'),
                         optional(macroTarget()),
                         attributeList(fromInline)
                       ),
                       markAsNotAMacro()
                     )
                   ),
                   this::checkInclude
                 )
               )
        ;
    }

    private void checkInclude(ActionContext ctx) {
        String name = ctx.getStringAttribute("macro", "name", null);
        if ("include".equals(name)) {
            String target = ctx.getStringAttribute("macro", "target", null);
            ctx.include(sourceResolver.resolve(target));
        }
    }

    private Rule macroName() {
        if (isCached("macroName")) return cached("macroName");

        return node("macroName",
                 action(namePrototype(),
                   ctx -> ctx.setAttributeOnParent("macro", "name", new String(ctx.extract()))
                 )
               )
        ;
    }

    private Rule macroTarget() {
        if (isCached("macroTarget")) return cached("macroTarget");

        return node("macroTarget",
                 action(oneOrMore(noneOf(" \r\n\t[")),
                   ctx -> ctx.setAttributeOnParent("macro", "target", new String(ctx.extract()))
                 )
               )
        ;
    }

    private Rule markAsNotAMacro() {
        return () -> ctx -> {
            int position = ctx.getIntAttribute("position", -1);
            ctx.findParentContextNode().getParent().setAttribute(position + ".not-a-macro", true);
            return false;
        };
    }

    public Rule attributeList(boolean fromInline) {

        String nameInCache = "attributeList." + (fromInline?"inline":"block");
        if (isCached(nameInCache)) return cached(nameInCache);

        int limit = fromInline?100:-1; // no limit when parsing attribute from block rules

        return node("attributeList", nameInCache,
                limitTo(
                    sequence(
                        ch('['),
                        firstOf(
                            sequence(
                                firstOf(
                                    namedAttribute(),
                                    sequence(
                                        positionalAttribute(),
                                        optional(idAttribute()),
                                        zeroOrMore(
                                            firstOf(
                                                roleAttribute(),
                                                optionAttribute()
                                            )
                                        )
                                    ),
                                    sequence(
                                        idAttribute(),
                                        zeroOrMore(
                                            firstOf(
                                                roleAttribute(),
                                                optionAttribute()
                                            )
                                        )
                                    ),
                                    oneOrMore(
                                        firstOf(
                                            roleAttribute(),
                                            optionAttribute()
                                        )
                                    )
                                ),
                                optional(blank()),// TODO replace,
                                zeroOrMore(
                                    sequence(
                                        ch(','),
                                        optional(blank()),// TODO replace,
                                        firstOf(
                                            namedAttribute(),
                                            positionalAttribute()
                                        ),
                                        optional(blank())// TODO replace
                                    )
                                )
                            ),
                            empty() // TODO replace with optional(sequence) ?
                        ),
                        ch(']'),
                        optional(blank()),// TODO replace
                        firstOf(
                            () -> ctx -> fromInline,
                            firstOf(
                                newLine(),
                                eoi()
                            )
                        )// TODO replace
                    ),
                limit)
        );
    }

    private Rule namedAttribute() {
        if (isCached("namedAttribute")) return cached("namedAttribute");

        return node("namedAttribute",
                sequence(
                    attributeName(),
                    ch('='),
                    firstOf(
                        sequence(
                            optional('"'),
                            attributeValue(true),
                            optional('"')
                        ),
                        attributeValue(true)
                    )
                )
        );
    }

    private Rule positionalAttribute() {
        if (isCached("positionalAttribute")) return cached("positionalAttribute");

        return node("positionalAttribute",
                sequence(
                    testNot(
                      sequence(
                          zeroOrMore(blank()),
                          ch(']')
                      )
                    ),
                    attributeValue(false)
                )
        );
    }

    private Rule idAttribute() {
        if (isCached("idAttribute")) return cached("idAttribute");

        return node("idAttribute",
                sequence(
                    ch('#'),
                    attributeName()
                )
        );
    }

    private Rule roleAttribute() {
        if (isCached("roleAttribute")) return cached("roleAttribute");

        return node("roleAttribute",
                sequence(
                    ch('.'),
                    attributeName()
                )
        );
    }

    private Rule optionAttribute() {
        if (isCached("optionAttribute")) return cached("optionAttribute");

        return node("optionAttribute",
                sequence(
                    ch('%'),
                    attributeName()
                )
        );
    }

    private Rule attributeName() {
        if (isCached("attributeName")) return cached("attributeName");

        return node("attributeName", namePrototype());
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
                            digitsOrLetters()
                        )
                    ),
                    optional(blank())
                )
        );
    }

    private Rule attributeValue(boolean withDot) {
        String name = "attributeValue" + (withDot?".withDot":"");

        if (isCached(name)) return cached(name);

        if (withDot) {
            return node("attributeValue", name,
                    oneOrMore(
                            //noneOf("[],")
                            noneOf("[]#%,\"") // TODO how to deal with #.%, characters ?
                    )
            );
        }

        return node("attributeValue", name,
                oneOrMore(
                        //noneOf("[],")
                        noneOf("[]#.%,\"") // TODO how to deal with #.%, characters ?
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

    public Rule digits() {
        if (isCached("digits")) return cached("digits");

        return cached("digits",
                oneOrMore(charRange('0', '9'))
        );
    }

    public Rule letters() {
        if (isCached("letters")) return cached("letters");

        return cached("letters",
                firstOf(
                    charRange('a', 'z'),
                    charRange('A', 'Z')
                )
        );
    }

    public Rule digitsOrLetters() {
        if (isCached("digitsOrLetters")) return cached("digitsOrLetters");

        return cached("digitsOrLetters",
                firstOf(
                    digits(),
                    letters()
                )
        );
    }

}
