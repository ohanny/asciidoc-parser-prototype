package fr.icodem.asciidoc.parser.block;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AdmonitionParagraphSpec.class,
    TableSpec.class,
    LabeledListSpec.class
})
public class BlockGrammarTestSuite {
}
