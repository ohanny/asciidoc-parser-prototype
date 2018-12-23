package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.listing.CalloutProcessorSpec;
import fr.icodem.asciidoc.parser.listing.CodeMarksProcessorSpec;
import fr.icodem.asciidoc.parser.listing.ListingProcessorSpec;
import fr.icodem.asciidoc.parser.listing.SplitLinesProcessorSpec;
import fr.icodem.asciidoc.parser.text.TextListenerDelegateSpec;
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
    CodeMarksProcessorSpec.class,
    ListingProcessorSpec.class,
    CalloutProcessorSpec.class,
    TextListenerDelegateSpec.class
})
public class ParserTestSuite {
}
