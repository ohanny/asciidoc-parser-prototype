package fr.icodem.asciidoc.parser.peg.listeners;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;

public class ToStringAnalysisBuilder implements ParsingProcessListener {

    private int level;

    private String getTab() {
        String tab = "";
        for (int i = 0; i < level; level++) {
            tab += "\t";
        }

        return tab;
    }

    @Override
    public void matcherContextLevel(int level) {
        this.level = level;
    }

    @Override
    public void matcherStart(Matcher matcher) {
        System.out.println(getTab() + matcher.getLabel());
    }

    @Override
    public void matcherEnd(Matcher matcher, boolean match) {

    }
}
