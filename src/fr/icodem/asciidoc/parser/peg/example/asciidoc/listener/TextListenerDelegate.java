package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

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

    public TextListenerDelegate(AsciidocHandler handler) {
        this.handler = handler;
    }

    public void text(String text) {
        handler.writeText(text);
    }

    // image
    @Override
    protected void image(ImageMacro macro) {

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
}
