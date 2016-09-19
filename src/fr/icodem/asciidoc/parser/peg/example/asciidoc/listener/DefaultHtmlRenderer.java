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
import java.util.stream.Collectors;

import static fr.icodem.asciidoc.backend.html.HtmlTag.*;

public class DefaultHtmlRenderer extends HtmlBaseRenderer {

    public static void main(String[] args) {
        String text =
                "= Hello\n" +
                "John Doe; Roger Rabbit <roger@mail.com>; François Pignon <fp@mail.com[@françois]>; Alice <http://www.gutenberg.org/cache/epub/11/pg11.txt[@alice]>\n" +
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

    private boolean hasHeader;
    private boolean hasPreamble;

    @Override
    public void writeText(String node, String text) {
        switch (node) {
            case DOCUMENT_TITLE:
                title = text;
                break;
            case PARAGRAPH:
                //include(() -> addFormattedText(p.getFormattedText()));
                break;
        }

        append(text);
    }

    @Override
    public void startDocument() {
        append(DOCTYPE.tag())
            .nl()
            .append(HTML.start())
            .nl()
            .append(HEAD.start())
            .nl()
            .incrementIndentLevel()
            .indent()
            .append(META.tag("charset", "UTF-8"))
            .nl()
            .indent()
            .append(META.tag("name", "viewport", "content", "width=device-width, initial-scale=1.0"))
            .nl()
            .indent()
            .append(META.tag("name", "generator", "content", "xxx"))
            .nl()
            .bufferOn()
            .mark("authors")
            .indent()
            .append(LINK.tag("rel", "stylesheet", "href", "styles.css"))
            .nl()
            .mark("title")
            .append(HEAD.end())
            .nl()
            .decrementIndentLevel()
            .append(BODY.start("class", getAttributeValue("doctype")))
            .nl()
            .incrementIndentLevel();
    }

    @Override
    public void endDocument() {
        decrementIndentLevel()
            .indent()
            .append(BODY.end())
            .nl()
            .decrementIndentLevel()
            .append(HTML.end());
    }

    @Override
    public void startHeader() {
        hasHeader = true;

        indent()
            .append(DIV.start("id", "header"))
            .nl()
            .incrementIndentLevel();
    }

    @Override
    public void endHeader() {
        decrementIndentLevel()
            .indent()
            .append(DIV.end())
            .nl()
            .moveTo("title")
            .indent()
            .append(TITLE.start())
            .append(title)
            .append(TITLE.end())
            .nl()
            .bufferOff();

        startContent();
    }

    @Override
    public void startDocumentTitle() {
        indent()
            .append(H1.start());
    }

    @Override
    public void endDocumentTitle() {
        append(H1.end())
            .nl();
    }

    @Override
    public void startAuthors() {
        indent()
            .append(DIV.start("class", "details"))
            .nl()
            .incrementIndentLevel();
        authors = new LinkedList<>();
    }

    @Override
    public void endAuthors() {
        String authors =
                this.authors
                    .stream()
                    .map(a -> a.name)
                    .collect(Collectors.joining(", "));

        decrementIndentLevel()
            .indent()
            .append(DIV.end())
            .nl()
            .moveTo("authors")
            .indent()
            .append(META.tag("name", "author", "content", authors))
            .nl()
            .moveLast();
    }

    @Override
    public void startAuthor() {
        authors.add(AuthorContext.withPosition(authors.size() + 1));
    }

    @Override
    public void endAuthor() {
        AuthorContext author = authors.peekLast();
        String index = author.position == 1?"":"" + author.position;

        indent()
            .append(SPAN.start("id", "author" + index, "class", "author"))
            .append(SPAN.end())
            .append(BR.tag())
            .nl()
            .runIf(author.address != null, () -> {
                String href = author.address;
                if (!(href.startsWith("http://") || href.startsWith("https://"))) {
                    href = "mailto:" + href;
                }

                String label = author.addressLabel != null ? author.addressLabel : author.address;

                indent()
                    .append(SPAN.start("id", "email" + index, "class", "email"))
                    .append(A.start("href", href))
                    .append(label)
                    .append(A.end())
                    .append(SPAN.end())
                    .append(BR.tag())
                    .nl();
            });
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

    @Override
    public void startPreamble() {
        hasPreamble = true;

        indent()
            .append(DIV.start("id", "preamble"))
            .incrementIndentLevel()
            .nl();
    }

    @Override
    public void endPreamble() {
        decrementIndentLevel()
            .indent()
            .append(DIV.end())
            .nl();
    }

    @Override
    public void startContent() {
        runIf(!hasHeader, () -> bufferOff())
            .runIf(!hasPreamble, () ->
                indent()
                .append(DIV.start("id", "content"))
                .incrementIndentLevel()
                .nl()
            );
    }

    @Override
    public void endContent() {
        decrementIndentLevel()
            .indent()
            .append(DIV.end())
            .nl();
    }

    @Override
    public void startParagraph() {
        indent()
            .append(DIV.start("class", "paragraph"))
            .nl()
            .incrementIndentLevel()
            .indent()
            .append(P.start());
    }

    @Override
    public void endParagraph() {
        append(P.end())
            .nl()
            .decrementIndentLevel()
            .indent()
            .append(DIV.end())
            .nl();
    }

}
