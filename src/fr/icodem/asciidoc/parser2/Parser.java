package fr.icodem.asciidoc.parser2;

import java.io.Reader;

public class Parser {

    private Reader reader;

    public void parse(Reader reader, ParserRule rule) {
        this.reader = reader;
        rule.parse();
    }

}
