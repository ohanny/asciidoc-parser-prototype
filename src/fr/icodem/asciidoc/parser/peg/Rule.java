package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;

/**
 * Rules are used to describe a grammar. Each rule supplies a matcher.
 */
@FunctionalInterface
public interface Rule {

    /**
     * Gets the {@link Matcher matcher} associated with the rule
     * @return the associated matcher
     */
    Matcher getMatcher();

    /**
     * Gets the name of the rule. The name may be used to cache rule instances.
     * When null is returned, the rule cannot be cached.
     * @return the name of the rule
     */
    default String getName() {
        return null;
    }

    /**
     * Indicates whether the rule should lead to a {@link Node node} creation
     * @return true if node creation is required
     */
    @Deprecated
    default boolean isNode() {//TODO unused ?
        return false;
    }

}
