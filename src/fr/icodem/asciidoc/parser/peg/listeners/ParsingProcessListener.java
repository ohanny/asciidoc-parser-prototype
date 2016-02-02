package fr.icodem.asciidoc.parser.peg.listeners;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;

public interface ParsingProcessListener {
    void matcherStart(Matcher matcher, int level, int position);
    void nextChar(char c);
    void matcherEnd(Matcher matcher, boolean match);
}
