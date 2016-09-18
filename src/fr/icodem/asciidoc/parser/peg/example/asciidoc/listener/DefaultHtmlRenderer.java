package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.backend.html.HtmlBackend;
import fr.icodem.asciidoc.parser.AsciidocPegProcessor;
import fr.icodem.asciidoc.parser.elements.AttributeEntry;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static fr.icodem.asciidoc.backend.html.HtmlTag.*;

public class DefaultHtmlRenderer extends HtmlBaseRenderer {

    public static void main(String[] args) {
        String text = "= Hello\n" +
                "John Doe; Roger Rabbit <roger@mail.com>; François Pignon <fp@mail.com[@françois]>\n" +
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

    private String title;

    private static class AuthorContext {// TODO move class ?

        private AuthorContext(int position) {
            this.position = position;
        }

        public static AuthorContext withPosition(int position) {
            return new AuthorContext(position);
        }

        int position;
        String name;
        String address;
        String addressLabel;
    }

    private Deque<AuthorContext> authors;

    @Override
    public void writeText(String node, String text) {
        switch (node) {
            case DOCUMENT_TITLE:
                title = text;
                break;
            case AUTHOR_ADDRESS:
                append(A.start("href", "mailto:" + text))
                        .append(text).append(A.end())
                        .append(SPAN.end()).append(BR.tag()).nl();
                break;
        }

        append(text);
    }

    @Override
    public void startDocument() {
        append(DOCTYPE.tag()).nl()
                .append(HTML.start()).nl()
                .append(HEAD.start()).nl().incrementIndentLevel()
                .indent().append(META.tag("charset", "UTF-8")).nl()
                .indent().append(META.tag("name", "viewport", "content", "width=device-width, initial-scale=1.0")).nl()
                .indent().append(META.tag("name", "generator", "content", "xxx")).nl()
                .bufferOn()
                .indent().append(LINK.tag("rel", "stylesheet", "href", "styles.css")).nl()
                .mark("authors")
                .mark("title")
                .append(HEAD.end()).nl().decrementIndentLevel()
                .append(BODY.start("class", getAttributeValue("doctype"))).nl().incrementIndentLevel();
    }

    @Override
    public void endDocument() {
        //decrementIndentLevel().indent().append(DIV.end()).nl() // content end
        decrementIndentLevel().indent()
                .append(BODY.end()).nl()
                .decrementIndentLevel().append(HTML.end());
    }

    @Override
    public void startHeader() {
        indent().append(DIV.start("id", "header")).nl().incrementIndentLevel();
    }

    @Override
    public void endHeader() {
        decrementIndentLevel().indent().append(DIV.end()).nl()
                //.insertAt("title", TITLE.start() + title + TITLE.end())
                .moveTo("title")
                .indent().append(TITLE.start()).append(title).append(TITLE.end()).nl()
                .bufferOff();
    }

    @Override
    public void startDocumentTitle() {
        indent().append(H1.start());
    }

    @Override
    public void endDocumentTitle() {
        append(H1.end()).nl();
    }

    @Override
    public void startAuthors() {
        indent().append(DIV.start("class", "details")).nl().incrementIndentLevel();
        authors = new LinkedList<>();
    }

    @Override
    public void endAuthors() {
        decrementIndentLevel().indent().append(DIV.end()).nl();
    }

    @Override
    public void startAuthor() {
        authors.add(AuthorContext.withPosition(authors.size() + 1));
        /*
                .forEach(header.getAuthors(), a -> {
            String index = "" + ((a.getPosition() == 1) ? "" : "" + a.getPosition());
            indent().append(SPAN.start("id", "author" + index, "class", "author"))
                    .append(a.getName()).append(SPAN.end()).append(BR.tag()).nl()
                    .runIf(a.getAddress() != null, () -> {
                        indent().append(SPAN.start("id", "email" + index, "class", "email"))
                                .append(A.start("href", "mailto:" + a.getAddress()))
                                .append(a.getAddress()).append(A.end())
                                .append(SPAN.end()).append(BR.tag()).nl();
                    });
        })
        */

    }

    @Override
    public void endAuthor() {
        AuthorContext author = authors.peekLast();
        String index = author.position == 1?"":"" + author.position;
        indent().append(SPAN.start("id", "author" + index, "class", "author"));
        append(SPAN.end()).append(BR.tag()).nl();

        if (author.address != null) {
            indent().append(SPAN.start("id", "email" + index, "class", "email"))
                    .append(A.start("href", "mailto:" + author.address))
                    .append(author.addressLabel != null ? author.addressLabel : author.address).append(A.end());
            append(SPAN.end()).append(BR.tag()).nl();
        }
    }

    @Override
    public void writeAuthorName(String name) {
        authors.peekLast().name = name;
    }

    @Override
    public void writeAuthorAddress(String address) {
        authors.peekLast().address = address;
    }

    @Override
    public void writeAuthorAddressLabel(String label) {
        authors.peekLast().addressLabel = label;
    }

//    @Override
//    public void startAuthorName() {//OK
//        String index = "";
//        indent().append(SPAN.start("id", "author" + index, "class", "author"));
//    }
//
//    @Override
//    public void endAuthorName() {//OK
//        append(SPAN.end()).append(BR.tag()).nl();
//    }
//
//    @Override
//    public void startAuthorAddress() {
//        String index = "";
//        indent().append(SPAN.start("id", "email" + index, "class", "email"));
//                //.append(A.start("href", "mailto:" + a.getAddress()))
//                //.append(a.getAddress()).append(A.end());
//    }
//
//    @Override
//    public void endAuthorAddress() {//OK
//        append(SPAN.end()).append(BR.tag()).nl();
//    }
}
