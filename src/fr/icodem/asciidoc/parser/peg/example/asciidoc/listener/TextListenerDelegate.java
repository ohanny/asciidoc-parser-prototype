package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.UrlUtils;

public abstract class TextListenerDelegate extends AbstractDelegate {

    protected MarkContext currentMark;

    public static class MarkContext {
        AttributeList attributeList;

        static MarkContext of(AttributeList attList) {
            MarkContext mark = new MarkContext();
            mark.attributeList = attList;
            return mark;
        }

        public AttributeList getAttributeList() {
            return attributeList;
        }
    }

    XRefContext currentXRef;
    protected static class XRefContext {
        String label;
        String value;
        boolean internal;

        static XRefContext empty() {
            return new XRefContext();
        }
    }

    public TextListenerDelegate(AttributeEntries attributeEntries) {
        super(attributeEntries);
    }

    public void text(String text) {
        text = text.replace("\\_", "_");

        writeText(text);
    }

    protected void writeText(String text) {}

    public void xmlEntity(String text) {
        writeText(text);
    }

    // image
    @Override
    protected void image(ImageMacro macro) {

    }

    @Override
    protected void video(VideoMacro macro) {

    }

    // markup methods
    public void enterBold() {}

    public void exitBold() {}

    public void enterItalic() {}

    public void exitItalic() {}

    public void enterSubscript() {}

    public void exitSubscript() {}

    public void enterSuperscript() {}

    public void exitSuperscript() {}

    public void enterMonospace() {}

    public void exitMonospace() {}

    public void enterMark() {
        currentMark = MarkContext.of(consumeAttList());
    }

    public void exitMark() {
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
        currentXRef = null;
    }


}
