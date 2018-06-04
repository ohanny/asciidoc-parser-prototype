package fr.icodem.asciidoc.parser.peg.example.asciidoc;

import fr.icodem.asciidoc.parser.peg.BaseRules;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory;

import java.util.function.Supplier;

public class HighlightRules extends BaseRules {
    private AttributeEntries attributeEntries;
    private CommonRules commonRules;

    public HighlightRules(AttributeEntries attributeEntries) {
        this.attributeEntries = attributeEntries;
        this.commonRules = new CommonRules(attributeEntries);
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
                 ch('m')
               )
        ;
    }

    private Rule strong() {
        return node("strong",
                 ch('s')
               )
        ;
    }

}
