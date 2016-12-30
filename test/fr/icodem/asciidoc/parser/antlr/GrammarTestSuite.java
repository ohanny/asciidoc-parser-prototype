package fr.icodem.asciidoc.parser.antlr;

import fr.icodem.asciidoc.parser.block.BlockGrammarTestSuite;
import fr.icodem.asciidoc.parser.text.TextGrammarTestSuite;
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
    ListingBlockTest.class,
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
    BlockGrammarTestSuite.class,
    TextGrammarTestSuite.class
})
public class GrammarTestSuite {
}
