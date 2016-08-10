package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.antlr.GrammarTestSuite;
import fr.icodem.asciidoc.parser.peg.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GrammarTestSuite.class,
        ParserTestSuite.class,
        QuotedTextSpec.class,
        CustomStylingWithAttributeSpec.class,
        ReplacementTextSpec.class,
        InlineMacroSpec.class
})
public class AllTestSuite {
}
