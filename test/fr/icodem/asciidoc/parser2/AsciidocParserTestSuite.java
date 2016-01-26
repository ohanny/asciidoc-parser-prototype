package fr.icodem.asciidoc.parser2;

import org.junit.Ignore;
import org.junit.Test;

import java.io.StringReader;

public class AsciidocParserTestSuite {

    private String toStringTree() {
        return null;
    }

    @Test @Ignore
    public void test() throws Exception {

        StringReader reader = new StringReader("= Hello");

        AsciidocParser parser = new AsciidocParser();
        parser.parse(reader, parser::document);

    }

    @Test
    public void test2() throws Exception {

        StringReader reader = new StringReader("= Hello");

        AsciidocParser2 parser = new AsciidocParser2(reader);
        parser.document();

    }

}
