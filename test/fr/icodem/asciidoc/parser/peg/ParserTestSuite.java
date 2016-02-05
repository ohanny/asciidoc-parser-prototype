package fr.icodem.asciidoc.parser.peg;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    InputBufferTest.class,
    ParserTest.class,
    FlushingTest.class,
    CalculatorTest.class
})
public class ParserTestSuite {
}
