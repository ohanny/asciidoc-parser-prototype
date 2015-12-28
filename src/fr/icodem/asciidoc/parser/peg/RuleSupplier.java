package fr.icodem.asciidoc.parser.peg;

/**
 * Represent a supplier of rules.
 */
@FunctionalInterface
public interface RuleSupplier {

    /**
     * Gets the rule
     * @return the supplied rule
     */
    Rule getRule();
}
