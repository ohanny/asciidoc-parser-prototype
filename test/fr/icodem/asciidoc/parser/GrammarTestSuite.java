package fr.icodem.asciidoc.parser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TitlesOnlyTest.class,
    HeaderWithContentTest.class,
    HeaderAndOneSectionWithContentTest.class,
    NoHeaderAndOneSectionWithContentTest.class,
    SingleCommentTest.class,
    HeaderWithSingleCommentTest.class,
    MultiCommentTest.class,
    HeaderWithMultiCommentTest.class,
    ParagraphTest.class,
    BlockIdTest.class
})
public class GrammarTestSuite {
}
