package fr.icodem.asciidoc.parser.antlr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AdmonitionSpec.class,
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
    OrderedListTest.class,
    UnorderedListWithComplexContentTest.class,
    AttributeListTest.class,
    BlockTitleTest.class,
    BlockMacroTest.class,
    HorizontalRuleTest.class,
    SimpleTableTest.class,
    CellFormatingTableTest.class,
    AuthorLineTest.class,
    PreambleTest.class,
    QuotedTextSpec.class,
    CustomStylingWithAttributeSpec.class,
    ReplacementTextSpec.class,
    InlineMacroSpec.class
})
public class GrammarTestSuite {
}
