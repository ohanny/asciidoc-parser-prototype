package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.antlr.AsciidocBaseListener;
import fr.icodem.asciidoc.parser.antlr.AsciidocParser;
import fr.icodem.asciidoc.parser.elements.ElementFactory;
import org.antlr.v4.runtime.tree.TerminalNode;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AsciidocDocumentProcessor extends AsciidocBaseListener {

    private AsciidocParserHandler handler;
    private ElementFactory ef;

    //private Queue<Element> elementQueue;

//    PrintWriter writer = new PrintWriter(new StringWriter());

    //private final static String NL = "\r\n";
    //private final static String INDENT = "  ";

    //private int indentLevel = 0;

//    private Writer writer = new StringWriter();

    public AsciidocDocumentProcessor(AsciidocParserHandler handler) {
        this.handler = handler;
        if (handler == null) throw new IllegalArgumentException("Handler must not be null");
        this.ef = new ElementFactory();
        //this.elementQueue = new LinkedList<>();
    }

//    private AsciidocDocumentProcessor append(String str) {
//        try {
//            writer.write(str);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return this;
//    }
//
//    private AsciidocDocumentProcessor nl() {
//        append(NL);
//        return this;
//    }
//
//    private AsciidocDocumentProcessor indent() {
//        return indent(indentLevel);
//    }
//
//    private AsciidocDocumentProcessor indent(int times) {
//        for (int i = 0; i < times; i++) {
//            append(INDENT);
//        }
//        return this;
//    }
//
//    private AsciidocDocumentProcessor incrementIndentLevel() {
//        indentLevel++;
//        return this;
//    }
//
//    private AsciidocDocumentProcessor decrementIndentLevel() {
//        indentLevel--;
//        return this;
//    }

    @Override
    public void enterDocument(AsciidocParser.DocumentContext ctx) {
        handler.startDocument(ef.document());
//        append("<!DOCTYPE html>").nl()
//                .append("<html>").nl()
//                .append("<head>").nl().incrementIndentLevel()
//                .indent().append("<meta charset=\"UTF-8\">").nl()
//                .append("</head>").nl().decrementIndentLevel()
//                .append("<body>").nl().incrementIndentLevel();
    }

    @Override
    public void exitDocument(AsciidocParser.DocumentContext ctx) {
        handler.endDocument(ef.document());
//        append("</body>").nl().decrementIndentLevel()
//                .append("</html>");
//
//        System.out.println(writer.toString());
    }

    @Override
    public void enterDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
//        indent().append(H1.start());
        handler.startDocumentTitle(ef.documentTitle());
    }

    @Override
    public void exitDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
        handler.endDocumentTitle(ef.documentTitle());
//        append(H1.end()).nl();
    }

    @Override
    public void enterTitle(AsciidocParser.TitleContext ctx) {
        final String text = ctx.getText();
        handler.startTitle(ef.title(text));
//        append(ctx.getText());
    }

    @Override
    public void enterParagraph(AsciidocParser.ParagraphContext ctx) {
        String text = ctx.getText().trim();
        handler.startParagraph(ef.paragraph(text));
//        indent().append("<p>").append(ctx.getText().trim()).append("</p>").nl();
    }

    @Override
    public void enterSection(AsciidocParser.SectionContext ctx) {
        handler.startSection(ef.section());
//        indent().append("<section>").nl().incrementIndentLevel();
    }

    @Override
    public void exitSection(AsciidocParser.SectionContext ctx) {
        handler.endSection(ef.section());
//        decrementIndentLevel().indent().append("</section>").nl();
    }

    @Override
    public void enterSectionTitle(AsciidocParser.SectionTitleContext ctx) {
        int level = max(min(ctx.EQ().size(), 6), 1);
        handler.startSectionTitle(ef.sectionTitle(level));
//        indent().append(getTitleHeader(titleLevel).start());
    }

    @Override
    public void exitSectionTitle(AsciidocParser.SectionTitleContext ctx) {
        int level = min(ctx.EQ().size(), 6);
        handler.endSectionTitle(ef.sectionTitle(level));
//        append(getTitleHeader(titleLevel).end()).nl();
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        //System.out.println(node.getSymbol());
        //System.out.println(node.getText());
    }

}
