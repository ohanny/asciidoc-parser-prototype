package fr.icodem.asciidoc.parser.peg.listeners;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;

public class DefaultParsingProcessListener implements ParsingProcessListener {

    @Override
    public void matcherStart(Matcher matcher, int level, int position) {}

    @Override
    public void nextChar(char c) {}

    @Override
    public void matcherEnd(Matcher matcher, boolean match) {}
}
