package fr.icodem.asciidoc.parser.peg.listeners;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;

public class DefaultParsingProcessListener implements ParsingProcessListener {
    @Override
    public void matcherContextLevel(int level) {}

    @Override
    public void matcherStart(Matcher matcher) {}

    @Override
    public void matcherEnd(Matcher matcher, boolean match) {}
}
