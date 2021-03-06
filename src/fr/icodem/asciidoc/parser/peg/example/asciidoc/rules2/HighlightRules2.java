package fr.icodem.asciidoc.parser.peg.example.asciidoc.rules2;

import fr.icodem.asciidoc.parser.peg.BaseRules;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory;

public class HighlightRules2 extends BaseRules {
    private AttributeEntries attributeEntries;
    private CommonRules2 commonRules;

    public HighlightRules2(AttributeEntries attributeEntries) {
        this.attributeEntries = attributeEntries;
        this.commonRules = new CommonRules2(attributeEntries);
    }

    @Override
    public void withFactory(RulesFactory factory) {
        super.withFactory(factory);
        commonRules.withFactory(factory);
    }

    // common rules
    private Rule digits() {
        return commonRules.digits();
    }

    // main rule
    public Rule highlights() {
        return node("highlights",
                 oneOrMore(
                   parameterSet()
                 )
               )
        ;
    }

    private Rule parameterSet() {
        return node("parameterSet",
                 sequence(
                   optional(not()),
                   firstOf(
                     sequence(
                       lineFrom(),
                       ch(':'),
                       columnFrom(),
                       ch('@'),
                       columnTo()
                     ),
                     sequence(
                       lineFrom(),
                       optional(
                         sequence(
                           ch(':'),
                           columnFrom()
                         )
                       ),
                       optional(
                         sequence(
                           ch('-'),
                           lineTo(),
                           optional(
                             sequence(
                               ch(':'),
                               columnTo()
                             )
                           )
                         )
                       )
                     )
                   ),
                   optional(
                     firstOf(
                       important(),
                       comment(),
                       mark(),
                       strong()
                     )
                   ),
                   optional(ch(';'))
                 )
               )
        ;
    }

    @Deprecated
    public Rule parameterSet2() {
        return node("parameterSet",
                 sequence(
                   optional(not()),
                   lineFrom(),
                   optional(
                     sequence(
                       ch('-'),
                       lineTo()
                     )
                   ),
                   optional(
                     sequence(
                       ch(':'),
                       columnFrom(),
                       optional(
                         sequence(
                           ch('-'),
                           columnTo()
                         )
                       )
                     )
                   ),
                   optional(
                     firstOf(
                       important(),
                       comment(),
                       mark()
                     )
                   ),
                   optional(ch(';'))
                 )
               )
        ;
    }

    private Rule lineFrom() {
        return node("lineFrom",
                 digits()
               )
        ;
    }

    private Rule lineTo() {
        return node("lineTo",
                 digits()
               )
        ;
    }

    private Rule columnFrom() {
        return node("columnFrom",
                 digits()
               )
        ;
    }

    private Rule columnTo() {
        return node("columnTo",
                 digits()
               )
        ;
    }

    private Rule not() {
        return node("not",
                 ch('!')
               )
        ;
    }

    private Rule important() {
        return node("important",
                 ch('!')
               )
        ;
    }

    private Rule comment() {
        return node("comment",
                 ch('c')
               )
        ;
    }

    private Rule mark() {
        return node("mark",
                 sequence(
                   ch('m'),
                   optional(markLevel())
                 )
               )
        ;
    }

    private Rule markLevel() {
        return node("markLevel",
                 anyOf("123456789")
               )
        ;
    }

    private Rule strong() {
        return node("strong",
                 sequence(
                   ch('s'),
                   optional(strongLevel())
                 )
               )
        ;
    }

    private Rule strongLevel() {
        return node("strongLevel",
                anyOf("123456789")
               )
        ;
    }

}
