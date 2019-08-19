package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2;

public interface InlineHandler2 {
    default void text(String text) {
        text = text.replace("\\_", "_");

        writeText(text);
    }

    default void writeText(String text) {}

    default void xmlEntity(String text) {
        writeText(text);
    }

    // attributes
    default void attributeName(String value) {}

    default void attributeValue(String value) {}

    default void enterIdAttribute() {}

    default void enterRoleAttribute() {}

    default void enterOptionAttribute() {}

    default void enterPositionalAttribute() {}

    default void enterNamedAttribute() {}


    // image
    default void image() {}

    // markup methods
    default void enterBold() {}

    default void exitBold() {}

    default void enterItalic() {}

    default void exitItalic() {}

    default void enterSubscript() {}

    default void exitSubscript() {}

    default void enterSuperscript() {}

    default void exitSuperscript() {}

    default void enterMonospace() {}

    default void exitMonospace() {}

    default void enterMark() {}

    default void exitMark() {}

    default void enterXRef() {}

    default void xrefValue(String value) {}

    default void xrefLabel(String label) {}

    default void exitXRef() {}

}
