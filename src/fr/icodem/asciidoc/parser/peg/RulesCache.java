package fr.icodem.asciidoc.parser.peg;

import java.util.HashMap;
import java.util.Map;

/**
 * A cache for named rules
 */
public class RulesCache {

    // cache object
    private Map<String, Rule> nameToRuleCache = new HashMap<>();

    /**
     * Gets a rule from cache. If rule is not yet in cache, then
     * creates it from supplier and store it in cache.
     * @param name the name of the rule, should not be null otherwise
     *             an IllegalArgumentException is thrown
     * @param supplier used to create instance of rule if not in cache
     * @return the named rule
     */
    public Rule get(String name, RuleSupplier supplier) {
        Rule rule = nameToRuleCache.get(name);
        if (rule == null) {
            if (supplier == null) {
                throw new IllegalArgumentException("Rule supplier must not be null");
            }
            rule = supplier.getRule();
            if (rule.getName() == null) {
                throw new IllegalArgumentException("Supplied rule should get a name, but was null");
            }
            nameToRuleCache.put(name, rule);
        }
        return rule;
    }

    /**
     * Gets a rule from cache. If not found in cache, returns null
     * @param name the name of the rule to fetch from cache
     * @return the rule found in cache. If not found, null is returned
     */
    public Rule get(String name) {
        return nameToRuleCache.get(name);
    }

}
