package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.backend.html.HtmlBackend;
import fr.icodem.asciidoc.parser.elements.AttributeEntry;
import org.junit.Test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class HeaderTest {

    @Test
    public void test() throws Exception {

        String text = "//= Hello\n" +
                      "John Doe; Roger Rabbit <roger@mail.com>\n" +
                      ":fruit: kiwi\n" +
                      ":fruit2!:\n" +
                      ":!fruit3:\n" +
                      "\n" +
                      "The sun, the earth and the sea.\n" +
                      "\n" +
                      "== About fruits\n" +
                      "\n" +
                      ":fruit: banana\n" +
                      ":fruit2!:\n" +
                      ":!fruit3:\n" +
                      "\n" +
                      "[quote#think, Donald Trump]\n" +
                      "I love fruits\n" +
                      "\n" +
                      "[.summary.incremental%header%footer,xxx=yyy,xxx=zzz]\n" +
                      "* Apple\n" +
                      "* Kiwi\n" +
                      "* Banana\n";

        List<AttributeEntry> attributes = new ArrayList<>();

        StringWriter writer = new StringWriter();
        new AsciidocAntlrProcessor(new HtmlBackend(writer), attributes).parse(text);

        System.out.println(writer);

    }
}
