package fr.icodem.asciidoc.parser.peg.rules;

import fr.icodem.asciidoc.parser.peg.action.Action;
import fr.icodem.asciidoc.parser.peg.matchers.ActionMatcher;
import fr.icodem.asciidoc.parser.peg.matchers.Matcher;

public class ActionRule implements Rule {

    private ActionMatcher matcher;

    public ActionRule(Rule rule, Action action) {
        this.matcher = new ActionMatcher(rule, action);
    }

    @Override
    public Matcher getMatcher() {
        return matcher;
    }

    @Override
    public String getName() {
        return "action";
    }
}
