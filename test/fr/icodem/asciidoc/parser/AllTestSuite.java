package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.antlr.GrammarTestSuite;
import fr.icodem.asciidoc.parser.listing.ListingProcessorSpec;
import fr.icodem.asciidoc.renderer.*;
import fr.icodem.asciidoc.parser.peg.ParserTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GrammarTestSuite.class,
        ParserTestSuite.class,
        RendererTestSuite.class
})
public class AllTestSuite {
}
