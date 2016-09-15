package fr.icodem.asciidoc.parser.backend;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        HeadingsSpec.class,
        UnorderedListSpec.class,
        FormattedTextSpec.class,
        ParagraphSpec.class
})
public class BackendTestSuite {
}
