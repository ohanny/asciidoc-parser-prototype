package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.action.Action;
import fr.icodem.asciidoc.parser.peg.action.ActionContext;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

public class ActionMatcher implements Matcher {
    private Rule rule;
    private Matcher delegate; // cache

    private Action action;

    public ActionMatcher(Rule rule, Action action) {
        this.rule = rule;
        this.action = action;
    }

    @Override
    public boolean match(MatcherContext context) {
        MatcherContext delegateContext = context.getSubContext();
        ActionContext actionContext = new ActionContext(context, delegateContext);
        actionContext.markStart();

        if (getDelegate().match(delegateContext)) {
            actionContext.markEnd();
            action.execute(actionContext);
            return true;
        }

        return false;
    }

//    private ActionContext actionContext;
//    public ActionContext getActionContext() {
//        if (actionContext == null) {
//            actionContext = new ActionContext(this);
//        }
//        return actionContext;
//    }


    private Matcher getDelegate() {
        if (delegate == null) {
            delegate = rule.getMatcher();
        }
        return delegate;
    }

    @Override
    public boolean isOptional() {
        return getDelegate().isOptional();
    }
}
