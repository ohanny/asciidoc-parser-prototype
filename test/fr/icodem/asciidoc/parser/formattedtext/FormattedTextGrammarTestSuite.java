package fr.icodem.asciidoc.parser.formattedtext;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    QuotedTextSpec.class,
    CustomStylingWithAttributeSpec.class,
    ReplacementTextSpec.class,
    InlineMacroSpec.class
})
public class FormattedTextGrammarTestSuite {
}
