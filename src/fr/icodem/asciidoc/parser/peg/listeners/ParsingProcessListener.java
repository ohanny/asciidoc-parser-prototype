package fr.icodem.asciidoc.parser.peg.listeners;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;

public interface ParsingProcessListener {
    void matcherContextLevel(int level);
    void matcherStart(Matcher matcher);
    void matcherEnd(Matcher matcher, boolean match);
}
