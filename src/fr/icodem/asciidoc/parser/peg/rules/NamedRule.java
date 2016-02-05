package fr.icodem.asciidoc.parser.peg.rules;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;
import fr.icodem.asciidoc.parser.peg.matchers.NamedMatcher;

/**
 * A wrapper rule that allows to define a name for a delegate rule.
 */
public class NamedRule implements Rule {

    protected String name;
    private Rule delegate;
    protected Matcher matcher; // caching

    public NamedRule(String name, Rule delegate) {
        this.name = name;
        this.delegate = delegate;
    }

    @Override
    public Matcher getMatcher() {
        if (matcher == null) {
            matcher = new NamedMatcher(name, delegate.getMatcher());
        }
        return matcher;
    }

    /**
     * @see Rule#getName()
     */
    @Override
    public String getName() {
        return name;
    }
}
