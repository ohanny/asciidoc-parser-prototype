package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.ImageMacro;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Deque;
import java.util.LinkedList;
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
                "include::file1.adoc[]\n"  +
                "\n" +
                "The sun, *the earth* and _the_ sea.\n" +
                "\n" +
                "The sun, ~the earth~ and ^the^ sea.\n" +
                "\n" +
                "The sun, `the earth` and [big]#the# sea.\n" +
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
                "== Other fruits\n" +
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
                ".. Kiwai\n" +
                "\n" +
                "Titre 1:: contenu 1\n" +
                "Titre 2:: contenu 2\n" +
                "Titre 3::\n" +
                "+\n" +
                "contenu 3\n" +
                "\n" +
                "Titre 4::\n" +
                "+\n" +
                "* Item 1\n" +
                "* Item 2\n" +
                "\n" +
                "NOTE: this is a note\n" +
                "\n" +
                "Paragraph #2\n" +
                "\n" +
                "include::file1.adoc[]\n" +
                "\n" +
                "image::sunset.jpg[Sunset]\n" +
                "\n";

        if (false) text =  "Block above\n" +
                "\n" +
                "include::file.adoc[]\n" +
                "\n" +
                "Block below";

        if (false) text =  "* A\n" +
                "\n" +
                "include::f[]\n" +
                "\n" +
                "* B\n";


        //String includedText = "\n\nLe *ciel* est bleu.\n\n";
//        String includedText = "\n* Le *ciel* est bleu.";
        String includedText = "\nX\n";

                //List<AttributeEntry> attributes = new ArrayList<>();

//        System.out.println("\r\nWITH PEG\r\n");
//        StringWriter writer = new StringWriter();
//        new AsciidocPegProcessor(new HtmlBackend(writer), attributes).parse(text);
//        System.out.println(writer);

        //System.out.println("\r\nWITH NEW PEG\r\n");
        StringWriter writer = new StringWriter();
        DefaultHtmlRenderer.withWriter(writer)
                           .withSourceResolver(name -> new StringReader(includedText))
                           .render(text);
        System.out.println(writer);

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
    public void writeText(String text) {
        append(text);
    }


    /* **********************************************/
    // Macro
    /* **********************************************/

    @Override
    public void writeImage(ImageMacro image) {
        indent()
          .append(DIV.start("class", "imageblock"))
          .nl()
          .incIndent()
          .indent()
          .append(DIV.start("class", "content"))
          .nl()
          .incIndent()
          .indent()
          .append(IMG.tag("src", image.getTarget(), "alt", image.getAlternateText()))
          .nl()
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
        ;
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
            .mark("text")
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
            .moveTo("text")
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
    public void writeDocumentTitle(String title) {
        this.title = title;
        append(title);
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

    /*
    <div class="admonitionblock note">
      <table>
        <tr>
          <td class="icon">
            <div class="title">Note</div>
          </td>
          <td class="content">
            une note
          </td>
        </tr>
    </table>
   </div>
     */

    @Override
    public void startParagraph(String admonition) {
        if (admonition == null) {
            indent()
              .append(DIV.start("class", "paragraph"))
              .nl()
              .incIndent()
              .indent()
              .append(P.start());
        } else {
            indent()
              .append(DIV.start("class", "admonitionblock " + admonition.toLowerCase()))
                .nl()
                .incIndent()
                .indent()
                .append(TABLE.start())
                .nl()
                .incIndent()
                .indent()
                .append(TR.start())
                .nl()
                .incIndent()
                .indent()
                .append(TD.start("class", "icon"))
                .nl()
                .incIndent()
                .indent()
                .append(DIV.start("class", "title"))
                .append("Note")
                .append(DIV.end())
                .nl()
                .decIndent()
                .indent()
                .append(TD.end())
                .nl()
                .indent()
                .append(TD.start("class", "content"))
                .nl()
                .incIndent()
                .indent();
        }
    }

    @Override
    public void endParagraph(String admonition) {
        if (admonition == null) {
            append(P.end())
              .nl()
              .decIndent()
              .indent()
              .append(DIV.end())
              .nl();
        } else {
            nl()
              .decIndent()
              .indent()
              .append(TD.end())
              .nl()
              .decIndent()
              .indent()
              .append(TR.end())
              .nl()
              .decIndent()
              .indent()
              .append(TABLE.end())
              .nl()
              .decIndent()
              .indent()
              .append(DIV.end())
              .nl();
        }
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
    public void startLabeledList() {
        indent()
            .append(DIV.start("class", "dlist"))
            .nl()
            .incIndent()
            .indent()
            .append(DL.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endLabeledList() {
        decIndent()
            .indent()
            .append(DL.end())
            .nl()
            .decIndent()
            .indent()
            .append(DIV.end())
            .nl();
    }

    @Override
    public void startLabeledListItemTitle() {
        indent()
            .append(DT.start("class", "hdlist1"));
    }

    @Override
    public void endLabeledListItemTitle() {
        append(DT.end())
            .nl();
    }

    @Override
    public void startLabeledListItemContent() {
        indent()
            .append(DD.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endLabeledListItemContent() {
        decIndent()
            .indent()
            .append(DD.end())
            .nl();
    }

    @Override
    public void startLabeledListItemSimpleContent() {
        indent()
            .append(P.start());
    }

    @Override
    public void endLabeledListItemSimpleContent() {
        append(P.end())
            .nl();
    }

    @Override
    public void startTable(AttributeList attList) {
        String cssClass = "tableblock frame-all grid-all";
        if (attList == null || !attList.hasOption("autowidth")) {
            cssClass += " spread";
        }

        indent()
            .append(TABLE.start("class", cssClass))
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
    public void startColumnGroup() {
        indent()
            .append(COLGROUP.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endColumnGroup() {
        decIndent()
            .indent()
            .append(COLGROUP.end())
            .nl();
    }

    @Override
    public void column(AttributeList attList, double width) {
        StringBuilder sb = new StringBuilder();
        if (attList == null || !attList.hasOption("autowidth")) {
            sb.append("width: " + width + "%;");
        }
        String style = sb.toString();

        indent()
            .runIf(style.length() > 0, () -> append(COL.tag("style", style)))
            .runIf(style.length() == 0, () -> append(COL.tag()))
            .nl();
    }

    @Override
    public void startTableBody() {
        indent()
            .append(TBODY.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endTableBody() {
        decIndent()
            .indent()
            .append(TBODY.end())
            .nl();
    }

    @Override
    public void startTableHeader() {
        indent()
            .append(THEAD.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endTableHeader() {
        decIndent()
            .indent()
            .append(THEAD.end())
            .nl();
    }

    @Override
    public void startTableHeaderCell() {
        indent()
            .append(TH.start("class", "tableblock halign-left valign-top"));
    }

    @Override
    public void writeTableHeaderCellContent(String text) {
        append(text);
    }

    @Override
    public void endTableHeaderCell() {
        append(TH.end())
                .nl();
    }

    @Override
    public void startTableFooter() {
        indent()
            .append(TFOOT.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endTableFooter() {
        decIndent()
            .indent()
            .append(TFOOT.end())
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
            .append(TD.start("class", "tableblock halign-left valign-top"));
//        indent()
//            .append(TD.start())
//            .nl()
//            .incIndent();
    }

    @Override
    public void writeTableCellContent(String text) {
        append(P.start("class", "tableblock"))
            .append(text)
            .append(P.end());
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

    @Override
    public void startSubscript() {
        append(SUB.start());
    }

    @Override
    public void endSubscript() {
        append(SUB.end());
    }

    @Override
    public void startSuperscript() {
        append(SUP.start());
    }

    @Override
    public void endSuperscript() {
        append(SUP.end());
    }

    @Override
    public void startMonospace() {
        append(CODE.start());
    }

    @Override
    public void endMonospace() {
        append(CODE.end());
    }

    @Override
    public void startMark(AttributeList attList) {
        if (attList == null || attList.getFirstPositionalAttribute() == null) {
            append(MARK.start());
        } else {
            append(SPAN.start("class", attList.getFirstPositionalAttribute()));
        }
    }

    @Override
    public void endMark(AttributeList attList) {
        if (attList == null || attList.getFirstPositionalAttribute() == null) {
            append(MARK.end());
        } else {
            append(SPAN.end());
        }
    }

}
