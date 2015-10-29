package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.backend.html.HtmlBackend;
import org.junit.Test;

import java.io.StringWriter;

public class HeaderTest {

    @Test
    public void test() throws Exception {

        String textl = "= Hello\n" +
                      "John Doe\n" +
                      ":fruit: kiwi\n" +
                      "\n" +
                      "The sun, the earth and the sea.\n" +
                      "\n" +
                      "== About fruits\n" +
                      "I love fruits\n";

        String text = "= Hello\n" +
                      "John Doe\n" +
                      ":fruit: kiwi\n";

        StringWriter writer = new StringWriter();
        new AsciidocAntlrProcessor(new HtmlBackend(writer)).parse(text);
        System.out.println(writer);

    }
}
