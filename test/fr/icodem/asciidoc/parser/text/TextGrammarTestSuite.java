package fr.icodem.asciidoc.parser.text;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    BasicTextSpec.class,
    QuotedTextSpec.class,
    CustomStylingWithAttributeSpec.class,
    ReplacementTextSpec.class,
    InlineMacroSpec.class,
    XRefSpec.class
})
public class TextGrammarTestSuite {
}
