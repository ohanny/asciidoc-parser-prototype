package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.backend.html.HtmlBackend;
import fr.icodem.asciidoc.parser.elements.AttributeEntry;
import org.junit.Test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class HeaderTest {

    @Test
    public void test() throws Exception {

        String text = "= Hello\n" +
                      "John Doe; Roger Rabbit <roger@mail.com>\n" +
                      ":fruit: kiwi\n" +
                      "\n" +
                      "The sun, the earth and the sea.\n" +
                      "\n" +
                      "== About fruits\n" +
                      "I love fruits\n";

        List<AttributeEntry> attributes = new ArrayList<>();

        StringWriter writer = new StringWriter();
        new AsciidocAntlrProcessor(new HtmlBackend(writer), attributes).parse(text);
        System.out.println(writer);

    }
}
