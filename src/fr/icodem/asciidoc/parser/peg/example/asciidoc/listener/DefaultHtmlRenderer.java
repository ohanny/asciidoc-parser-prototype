package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.backend.html.HtmlBackend;
import fr.icodem.asciidoc.parser.AsciidocPegProcessor;
import fr.icodem.asciidoc.parser.elements.AttributeEntry;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static fr.icodem.asciidoc.backend.html.HtmlTag.*;

public class DefaultHtmlRenderer extends HtmlBaseRenderer {

    public static void main(String[] args) {
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

        System.out.println("\r\nWITH PEG\r\n");
        StringWriter writer = new StringWriter();
        new AsciidocPegProcessor(new HtmlBackend(writer), attributes).parse(text);
        System.out.println(writer);

        System.out.println("\r\nWITH NEW PEG\r\n");
        StringWriter writer1 = new StringWriter();
        DefaultHtmlRenderer.withWriter(writer1).render(text);
        System.out.println(writer1);

    }

    private DefaultHtmlRenderer(Writer writer) {
        super(writer);
    }

    public static DefaultHtmlRenderer withWriter(Writer writer) {
        return new DefaultHtmlRenderer(writer);
    }

    @Override
    public void writeText(String text) {
        append(text);
    }

    @Override
    public void startDocument() {
        append(DOCTYPE.tag()).nl()
                .append(HTML.start()).nl();
    }

    @Override
    public void endDocument() {
        //decrementIndentLevel().indent().append(DIV.end()).nl() // content end
        //decrementIndentLevel().indent()
                //.append(BODY.end()).nl()
        decrementIndentLevel().append(HTML.end());
    }

    @Override
    public void startHeader() {
        append(HEAD.start()).nl().incrementIndentLevel()
            .indent().append(META.tag("charset", "UTF-8")).nl()
            .indent().append(META.tag("name", "viewport", "content", "width=device-width, initial-scale=1.0")).nl()
            .indent().append(META.tag("name", "generator", "content", "xxx")).nl()
            .indent().append(LINK.tag("rel", "stylesheet", "href", "styles.css")).nl();
    }

    @Override
    public void endHeader() {
        append(HEAD.end()).nl().decrementIndentLevel();
    }

    @Override
    public void startDocumentTitle() {
        indent().append(H1.start());
    }

    @Override
    public void endDocumentTitle() {
        append(H1.end()).nl();
    }
}
