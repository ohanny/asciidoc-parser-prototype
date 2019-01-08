package fr.icodem.asciidoc.parser.block;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AdmonitionParagraphSpec.class,
    AdmonitionBlockSpec.class,
    TableSpec.class,
    LabeledListSpec.class,
    SidebarBlockSpec.class
})
public class BlockGrammarTestSuite {
}
