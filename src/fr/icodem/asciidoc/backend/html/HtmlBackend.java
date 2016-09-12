package fr.icodem.asciidoc.backend.html;

import fr.icodem.asciidoc.parser.elements.*;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static fr.icodem.asciidoc.backend.html.HtmlTag.*;

public class HtmlBackend extends HtmlBaseBackend {

    public HtmlBackend(Writer writer) {
        super(writer);
    }

    private void addBlock(Block b) {
        if (b instanceof Paragraph) {
            addParagraph((Paragraph) b);
        }
    }

    @Override
    public void startDocument() {
        append(DOCTYPE.tag()).nl()
                .append(HTML.start()).nl()
                .append(HEAD.start()).nl().incrementIndentLevel()
                .indent().append(META.tag("charset", "UTF-8")).nl()
                .indent().append(META.tag("name", "viewport", "content", "width=device-width, initial-scale=1.0")).nl()
                .indent().append(META.tag("name", "generator", "content", "xxx")).nl()
                .indent().append(LINK.tag("rel", "stylesheet", "href", "styles.css")).nl();
    }

    @Override
    public void documentHeader(DocumentHeader header) {

        runIf(header.isAuthorsPresent(),
                () -> indent().append(META.tag("name", "author", "content",
                        header.getAuthors()
                                .stream()
                                .map(a -> a.getName())
                                .collect(Collectors.joining(", "))))
                        .nl())
                .indent().append(TITLE.start()).append(header.getTitle().getText()).append(TITLE.end()).nl()
                .append(HEAD.end()).nl().decrementIndentLevel()
                .append(BODY.start("class", header.getAttributeValue("doctype"))).nl().incrementIndentLevel()
                .runIf(header.isHeaderPresent(), () -> {
                    indent().append(DIV.start("id", "header")).nl().incrementIndentLevel()
                            .indent().append(H1.start()).append(header.getTitle().getText()).append(H1.end()).nl()
                            .runIf(header.isAuthorsPresent(), () -> {
                                indent().append(DIV.start("class", "details")).nl().incrementIndentLevel()
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
                                        .decrementIndentLevel().indent().append(DIV.end()).nl();
                            })
                            .decrementIndentLevel().indent().append(DIV.end()).nl();
                })
                .indent().append(DIV.start("id", "content")).incrementIndentLevel().nl();
    }

    @Override
    public void startPreamble() {
        indent().append(DIV.start("id", "preamble")).incrementIndentLevel().nl();
    }

    @Override
    public void endPreamble() {
        decrementIndentLevel().indent().append(DIV.end()).nl();
    }

    @Override
    public void endDocument() {
        decrementIndentLevel().indent().append(DIV.end()).nl() // content end
            .append(BODY.end()).nl()
            .decrementIndentLevel().append(HTML.end());

        closeWriter();
    }

    @Override
    public void startParagraph(Paragraph p) {
        addParagraph(p);
    }

    private void addParagraph(Paragraph p) {
        indent().append(DIV.start("class", "paragraph")).nl()
                .incrementIndentLevel()
                .indent().append(P.start())
                //.append(p.getText())
                .include(() -> addFormattedText(p.getFormattedText()))
                .append(P.end()).nl()
                .decrementIndentLevel()
                .indent().append(DIV.end())
                .nl();
    }

    private void addFormattedText(Text.FormattedText text) {
        //Text.TextItem item = text.getFirstItem();
        //while (item != null) { // TODO PERF : remettre while ?
        for (Text.FormattedTextIterator it = text.iterator(); it.hasNext();) {
            Text.TextItem item = it.next();
            System.out.println(item + " => " + item.getText() + " - head : " + item.getHead());
            if (item instanceof Text.TextItem && false) {

            }
            else if (item instanceof Text.BoldTextItem) { // TODO PERF : utiliser map ?
                append("<strong>");
            }
            else if (item instanceof Text.ItalicTextItem) {
                append("<em>");
            }

            append(item.getText());

            //if (it.isTail()) {
            if (item.isTail()) {
                Text.TextItem head = item.getHead();
                if (head instanceof Text.BoldTextItem) { // TODO PERF : utiliser map ?
                    append("</strong>");
                } else if (head instanceof Text.ItalicTextItem) {
                    append("</em>");
                }
            }
            //item = item.getNext();
        }
    }

    private void addList(AbstractList list) {
        if (list instanceof UnorderedList) {
            addUnorderedList((UnorderedList) list);
        }
        else if (list instanceof OrderedList) {
            addOrderedList((OrderedList) list);
        }
    }

    private void addUnorderedList(UnorderedList list) {
        indent().append(DIV.start("class", "ulist")).nl().incrementIndentLevel()
                .indent().append(UL.start()).nl().incrementIndentLevel()
                .forEach(list.getItems(), this::addListItem)
                .decrementIndentLevel().indent().append(UL.end()).nl()
                .decrementIndentLevel().indent().append(DIV.end()).nl();
    }

    private void addOrderedList(OrderedList list) {
        CssElement css = CssElement.getOrderedListNumerationStyle(list.getFirstPositionalAttribute());
        if (css == null) {
            css = CssElement.getOrderedListNumerationStyle(list.getLevel());
        }

        String divStyles = "olist " + css.getOrderedListNumerationStyleName();
        String olStyle = css.getOrderedListNumerationStyleName();
        String olType = css.getOrderedListNumerationType();

        indent().append(DIV.start("class", divStyles)).nl().incrementIndentLevel()
                .runIf(olType == null, () -> indent().append(OL.start("class", olStyle)).nl().incrementIndentLevel())
                .runIf(olType != null, () -> indent().append(OL.start("class", olStyle, "type", olType)).nl().incrementIndentLevel())
                .forEach(list.getItems(), this::addListItem)
                .decrementIndentLevel().indent().append(OL.end()).nl()
                .decrementIndentLevel().indent().append(DIV.end()).nl();
    }

    private void addListItem(ListItem li) {
        indent().append(LI.start()).nl().incrementIndentLevel()
                .indent().append(P.start()).append(li.getText())
                .append(P.end()).nl()
                .runIf(li.hasNestedList(), () -> addList(li.getNestedList()))
                .forEach(li.getBlocks(), this::addBlock)
                .decrementIndentLevel().indent().append(LI.end()).nl();
    }

    @Override
    public void visitList(AbstractList list) {
        System.out.println(list);
        addList(list);
    }

    @Override
    public void startSection(Section section) {
        indent().append(SECTION.start()).nl().incrementIndentLevel();
    }

    @Override
    public void endSection(Section section) {
        decrementIndentLevel().indent().append(SECTION.end()).nl();
    }

    @Override
    public void startSectionTitle(SectionTitle sectionTitle) {
        indent().append(getTitleHeader(sectionTitle.getLevel()).start())
                .append(sectionTitle.getText())
                .append(getTitleHeader(sectionTitle.getLevel()).end()).nl();
    }

}

