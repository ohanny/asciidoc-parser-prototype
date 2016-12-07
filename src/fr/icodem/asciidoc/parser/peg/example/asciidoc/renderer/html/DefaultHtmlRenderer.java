package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html;

import fr.icodem.asciidoc.parser.elements.AttributeEntry;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeList;

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
                "The sun, *the earth* and _the_ sea.\n" +
                "\n" +
                "'''\n" +
                "\n" +
                "== About fruits\n" +
                "\n" +
                "Block before rule \n" +
                "\n" +
                "'''\n" +
                "\n" +
                "Block below rule\n" +
                "\n" +
                ". Pomme\n" +
                ". Poire\n" +
                ".. Cerise\n" +
                ".. Kiwi\n" +
                "* Mangue\n" +
                "* Kiwai\n" +
                "\n" +
                ":fruit: banana\n" +
                ":fruit2!:\n" +
                ":!fruit3:\n" +
                "\n" +
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
                "*** Trois\n" +
                "*** Quatre\n" +
                "* Zéro\n" +
                "\n" +
                "[lowerroman.summary.incremental%header%footer,xxx=yyy,xxx=zzz]\n" +
                "** Apple\n" +
                //". Apple\n" +
                "** Kiwi\n" +
                "** Cherry\n" +
                "* Banana\n" +
                "[lowergreek]\n" +
                ". Lemon\n" +
                ". Strawberry\n" +
                ".. Kaki\n" +
                ".. Kiwai\n";

        String text1 =                 "Block above\n" +
                "\n" +
                "'''\n" +
                "\n" +
                "Block below";



                List<AttributeEntry> attributes = new ArrayList<>();

//        System.out.println("\r\nWITH PEG\r\n");
//        StringWriter writer = new StringWriter();
//        new AsciidocPegProcessor(new HtmlBackend(writer), attributes).parse(text);
//        System.out.println(writer);

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
        }

        append(text);
    }

    /* **********************************************/
    // Block
    /* **********************************************/

    @Override
    public void startDocument() {
        append(DOCTYPE.tag())
            .nl()
            .append(HTML.start())
            .nl()
            .append(HEAD.start())
            .nl()
            .incIndent()
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
            .decIndent()
            .append(BODY.start("class", getAttributeValue("doctype")))
            .nl()
            .incIndent();
    }

    @Override
    public void endDocument() {
        decIndent()
            .indent()
            .append(BODY.end())
            .nl()
            .decIndent()
            .append(HTML.end());
    }

    @Override
    public void startHeader() {
        hasHeader = true;

        indent()
            .append(DIV.start("id", "header"))
            .nl()
            .incIndent();
    }

    @Override
    public void endHeader() {
        decIndent()
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
        append(H1.end()).nl();
    }

    @Override
    public void startAuthors() {
        indent()
            .append(DIV.start("class", "details"))
            .nl()
            .incIndent();
        authors = new LinkedList<>();
    }

    @Override
    public void endAuthors() {
        String authors =
                this.authors
                    .stream()
                    .map(a -> a.name)
                    .collect(Collectors.joining(", "));

        decIndent()
            .indent()
            .append(DIV.end())
            .nl()
            .moveTo("authors")
            .indent()
            .append(META.tag("name", "author", "content", authors))
            .nl()
            .moveEnd();
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
            .incIndent()
            .nl();
    }

    @Override
    public void endPreamble() {
        decIndent()
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
                .incIndent()
                .nl()
            );
    }

    @Override
    public void endContent() {
        decIndent()
            .indent()
            .append(DIV.end())
            .nl();
    }

    @Override
    public void startSection() {
        indent()
            .append(SECTION.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endSection() {
        decIndent()
            .indent()
            .append(SECTION.end())
            .nl();
    }

    @Override
    public void startSectionTitle(int level) {
        indent().append(getTitleHeader(level).start());
    }

    @Override
    public void endSectionTitle(int level) {
        append(getTitleHeader(level).end())
            .nl();
    }

    @Override
    public void horizontalRule() {
        indent()
            .append(HR.tag())
            .nl();
    }

    @Override
    public void startParagraph() {
        indent()
            .append(DIV.start("class", "paragraph"))
            .nl()
            .incIndent()
            .indent()
            .append(P.start());
    }

    @Override
    public void endParagraph() {
        append(P.end())
            .nl()
            .decIndent()
            .indent()
            .append(DIV.end())
            .nl();
    }

    @Override
    public void startList() {
        bufferOn();
    }

    @Override
    public void endList() {
        bufferOff();
    }

    @Override
    public void startOrderedList(int level, AttributeList atts) {
        CssElement css = null;
        if (atts != null) {
            css = CssElement.getOrderedListNumerationStyle(atts.getFirstPositionalAttribute());
        }
        if (css == null) {
            css = CssElement.getOrderedListNumerationStyle(level);
        }

        String divStyles = "olist " + css.getOrderedListNumerationStyleName();
        String olStyle = css.getOrderedListNumerationStyleName();
        String olType = css.getOrderedListNumerationType();

        runIf(level > 1, () -> moveTo("BeforeEndLI" + (level-1)))
            .indent()
            .append(DIV.start("class", divStyles))
            .nl()
            .incIndent()
            .indent()
            .runIf(olType == null, () -> append(OL.start("class", olStyle)))
            .runIf(olType != null, () -> append(OL.start("class", olStyle, "type", olType)))
            .nl()
            .incIndent();




        /*
        indent().append(DIV.start("class", divStyles)).nl().incIndent()
                .runIf(olType == null, () -> indent().append(OL.start("class", olStyle)).nl().incIndent())
                .runIf(olType != null, () -> indent().append(OL.start("class", olStyle, "type", olType)).nl().incIndent())
                .forEach(list.getItems(), this::addListItem)
                .decIndent().indent().append(OL.end()).nl()
                .decIndent().indent().append(DIV.end()).nl();
                 */

    }

    @Override
    public void endOrderedList(int level) {
        decIndent()
            .indent()
            .append(OL.end())
            .nl()
            .decIndent()
            .indent()
            .append(DIV.end())
            .nl()
            .runIf(level > 1, () -> moveTo("AfterEndLI" + (level-1)));
    }

    @Override
    public void startUnorderedList(int level, AttributeList attList) {
        runIf(level > 1, () -> moveTo("BeforeEndLI" + (level-1)))
            .indent()
            .append(DIV.start("class", "ulist"))
            .nl()
            .incIndent()
            .indent()
            .append(UL.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endUnorderedList(int level) {
        decIndent()
            .indent()
            .append(UL.end())
            .nl()
            .decIndent()
            .indent()
            .append(DIV.end())
            .nl()
            .runIf(level > 1, () -> moveTo("AfterEndLI" + (level-1)));
    }

    @Override
    public void startListItem(int level) {
        indent()
            .append(LI.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endListItem(int level) {
        mark("BeforeEndLI" + level)
            .decIndent()
            .indent()
            .append(LI.end())
            .nl()
            .mark("AfterEndLI" + level);
    }

    @Override
    public void startListItemValue() {
        indent()
            .append(P.start());
    }

    @Override
    public void endListItemValue() {
        append(P.end())
            .nl();
    }

    @Override
    public void startTable() {
        indent()
            .append(TABLE.start("class", "tableblock frame-all grid-all spread"))
            .nl()
            .incIndent();
    }

    @Override
    public void endTable() {
        decIndent()
            .indent()
            .append(TABLE.end())
            .nl();
    }

    @Override
    public void startTableRow() {
        indent()
            .append(TR.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endTableRow() {
        decIndent().
            indent()
            .append(TR.end())
            .nl();
    }

    @Override
    public void startTableCell() {
        indent()
            .append(TD.start());
//        indent()
//            .append(TD.start())
//            .nl()
//            .incIndent();
    }

    @Override
    public void endTableCell() {
        append(TD.end())
            .nl();
//        decIndent()
//            .indent()
//            .append(TD.end())
//            .nl();
    }

    @Override
    public void writeTableBlock(String text) {
        append(text);
    }

    /* **********************************************/
    // Inline text
    /* **********************************************/

    @Override
    public void startBold() {
        append(STRONG.start());
    }

    @Override
    public void endBold() {
        append(STRONG.end());
    }

    @Override
    public void startItalic() {
        append(EM.start());
    }

    @Override
    public void endItalic() {
        append(EM.end());
    }

}
