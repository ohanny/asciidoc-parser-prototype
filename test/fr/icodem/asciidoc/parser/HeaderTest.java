package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.backend.html.HtmlBackend;
import fr.icodem.asciidoc.parser.elements.AttributeEntry;
import org.junit.Test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeaderTest {

    @Test
    public void test() throws Exception {

        String text = "= Hello\n" +
                      "John Doe; Roger Rabbit <roger@mail.com>\n" +
                      //":fruit: kiwi\n" +
                      //":fruit2!:\n" +
                      //":!fruit3:\n" +
                      "\n" +
                      "The sun, the earth and the sea.\n" +
                      "\n" +
                      "== About fruits\n" +
                      "\n" +
                      ":fruit: banana\n" +
                      ":fruit2!:\n" +
                      ":!fruit3:\n" +
                      "\n" +
                      //"[quote#think, Donald Trump]\n" +
                      "I love fruits\n" +
                      "\n" +
                      "* One\n" +
                      "+\n" +
                      "Un paragraphe\n" +
                      "\n" +
                      "* Two \n" +
                      "* Three\n" +
                      "\n" +
                      "\n" +
                      "** Un\n" +
                      "** Deux\n" +
                      "\n" +
                      "[lowerroman.summary.incremental%header%footer,xxx=yyy,xxx=zzz]\n" +
                      ". Apple\n" +
                      "** Kiwi\n" +
                      "** Cherry\n" +
                      "* Banana\n" +
                      "[lowergreek]\n" +
                      ". Lemon\n" +
                      ". Strawberry\n" +
                      ".. Un\n" +
                      ".. Deux\n";

        List<AttributeEntry> attributes = new ArrayList<>();

        StringWriter writer1 = new StringWriter();
        new AsciidocAntlrProcessor(new HtmlBackend(writer1), attributes).parse(text);
        System.out.println(writer1);

        System.out.println("\r\nWITH PEG\r\n");
        StringWriter writer = new StringWriter();
        new AsciidocPegProcessor(new HtmlBackend(writer), attributes).parse(text);
        System.out.println(writer);

        String a = "((<|^|>)\\.(<|^|>)|\\.(<|^|>)|(<|^|>))?";


        System.out.println("ANTLR == PEG -> " + writer.toString().equals(writer1.toString()));

        String str1 = writer1.toString();
        String str = writer.toString();
        for (int i = 0; i < str1.length(); i++) {
            if (str.charAt(i) != str1.charAt(i)) {
                System.out.print(str.charAt(i));
                return;
            }
            System.out.print(str.charAt(i));
        }


    }

    private boolean testxx() {
        "".matches("((\\d+\\.\\d+|\\.\\d+|\\d+)(\\*|\\+))?((<|^|>)\\.(<|^|>)|\\.(<|^|>)|(<|^|>))?[aehlmdsv]?");
        return false;

    }
}
