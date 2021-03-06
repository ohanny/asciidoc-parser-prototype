package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

/**
 * A matcher is responsible for executing a specific {@link Rule rule} instance.
 * It implements the rule type specific matching logic.
 */
@FunctionalInterface
public interface Matcher {

    /**
     * Tries a match on the given {@link MatcherContext matcher context}
     * @param context the matcher context
     * @return true if the match was successful
     */
    boolean match(MatcherContext context);

    default boolean isOptional() {
        return false;
    }

    default String getLabel() {
        return "anonymous";
    }

}
