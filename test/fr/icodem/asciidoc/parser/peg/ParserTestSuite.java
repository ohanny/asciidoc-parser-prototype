package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.listing.ListingProcessorSpec;
import fr.icodem.asciidoc.parser.listing.SplitLinesProcessorSpec;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    InputBufferTest.class,
    ParserTest.class,
    FlushingTest.class,
    FlushingWithReaderInputBufferTest.class,
    CalculatorTest.class,
    IncludeReaderTest.class,
    SplitLinesProcessorSpec.class,
    ListingProcessorSpec.class
})
public class ParserTestSuite {
}
