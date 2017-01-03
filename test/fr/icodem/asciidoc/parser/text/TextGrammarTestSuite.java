package fr.icodem.asciidoc.parser.text;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    QuotedTextSpec.class,
    CustomStylingWithAttributeSpec.class,
    ReplacementTextSpec.class,
    InlineMacroSpec.class,
    XrefSpec.class
})
public class TextGrammarTestSuite {
}
