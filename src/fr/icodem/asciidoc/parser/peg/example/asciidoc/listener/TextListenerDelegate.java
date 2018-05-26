package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.UrlUtils;

public class TextListenerDelegate extends AbstractDelegate {

    private AsciidocHandler handler;

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

    public TextListenerDelegate(AsciidocHandler handler, AttributeEntries attributeEntries) {
        super(attributeEntries);
        this.handler = handler;
    }

    public void text(String text) {
        text = text.replace("\\_", "_");

        handler.writeText(text);
    }

    public void xmlEntity(String text) {
        handler.writeText(text);
    }

    // image
    @Override
    protected void image(ImageMacro macro) {

    }

    @Override
    protected void video(VideoMacro macro) {

    }

    // markup methods
    public void enterBold() {
        handler.startBold();
    }

    public void exitBold() {
        handler.endBold();
    }

    public void enterItalic() {
        handler.startItalic();
    }

    public void exitItalic() {
        handler.endItalic();
    }

    public void enterSubscript() {
        handler.startSubscript();
    }

    public void exitSubscript() {
        handler.endSubscript();
    }

    public void enterSuperscript() {
        handler.startSuperscript();
    }

    public void exitSuperscript() {
        handler.endSuperscript();
    }

    public void enterMonospace() {
        handler.startMonospace();
    }

    public void exitMonospace() {
        handler.endMonospace();
    }

    public void enterMark() {
        currentMark = MarkContext.of(consumeAttList());
        handler.startMark(currentMark.attributeList);
    }

    public void exitMark() {
        handler.endMark(currentMark.attributeList);
        currentMark = null;
    }

    public void enterXRef() {
        currentXRef = XRefContext.empty();
    }

    public void xrefValue(String value) {
        currentXRef.value = value;
        currentXRef.internal = UrlUtils.isUrl(value);
    }

    public void xrefLabel(String label) {
        currentXRef.label = label;
    }

    public void exitXRef() {
        handler.xref(XRef.of(currentXRef.value, currentXRef.label, currentXRef.internal));
        currentXRef = null;
    }
}
