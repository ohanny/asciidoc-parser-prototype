package fr.icodem.asciidoc.parser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    SectionTitleTest.class,
    HeaderTest.class,
    HeaderAndOneSectionWithContentTest.class,
    NoHeaderAndOneSectionWithContentTest.class,
    SingleCommentTest.class,
    HeaderWithSingleCommentTest.class,
    MultiCommentTest.class,
    HeaderWithMultiCommentTest.class,
    ParagraphTest.class,
    SourceBlockTest.class,
    LiteralBlockTest.class,
    BlockIdTest.class,
    RevisionInfoTest.class,
    AttributeEntryTest.class,
    UnorderedListTest.class,
    UnorderedListWithComplexContentTest.class,
    AttributeListTest.class,
    BlockTitleTest.class,
    BlockMacroTest.class,
    HorizontalRuleTest.class,
    SimpleTableTest.class,
    AuthorLineTest.class,
    PreambleTest.class
})
public class GrammarTestSuite {
}
