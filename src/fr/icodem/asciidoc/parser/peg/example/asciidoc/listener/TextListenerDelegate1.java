package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.UrlUtils;


/**
 * Delegates to Asciidoc handler
 */
public class TextListenerDelegate1 extends TextListenerDelegate {

    private AsciidocHandler handler;

    /*
    private MarkContext currentMark;

    private static class MarkContext {
        AttributeList attributeList;

        static MarkContext of(AttributeList attList) {
            MarkContext mark = new MarkContext();
            mark.attributeList = attList;
            return mark;
        }
    }

    XRefContext currentXRef;
    private static class XRefContext {
        String label;
        String value;
        boolean internal;

        static XRefContext empty() {
            return new XRefContext();
        }
    }
    */

    public TextListenerDelegate1(AsciidocHandler handler, AttributeEntries attributeEntries) {
        super(attributeEntries);
        this.handler = handler;
    }

    /*
    public void text(String text) {
        text = text.replace("\\_", "_");

        handler.writeText(text);
    }
    */

    @Override
    protected void writeText(String text) {
        handler.writeText(text);
    }

//    @Override
//    public void xmlEntity(String text) {
//        handler.writeText(text);
//    }


    // markup methods
    @Override
    public void enterBold() {
        handler.startBold();
    }

    @Override
    public void exitBold() {
        handler.endBold();
    }

    @Override
    public void enterItalic() {
        handler.startItalic();
    }

    @Override
    public void exitItalic() {
        handler.endItalic();
    }

    @Override
    public void enterSubscript() {
        handler.startSubscript();
    }

    @Override
    public void exitSubscript() {
        handler.endSubscript();
    }

    @Override
    public void enterSuperscript() {
        handler.startSuperscript();
    }

    @Override
    public void exitSuperscript() {
        handler.endSuperscript();
    }

    @Override
    public void enterMonospace() {
        handler.startMonospace();
    }

    @Override
    public void exitMonospace() {
        handler.endMonospace();
    }

    public void enterMark() {
        super.enterMark();
        handler.startMark(currentMark.attributeList);
    }

    public void exitMark() {
        handler.endMark(currentMark.attributeList);
        super.exitMark();
    }

    public void exitXRef() {
        handler.xref(XRef.of(currentXRef.value, currentXRef.label, currentXRef.internal));
        super.exitXRef();
    }
}
