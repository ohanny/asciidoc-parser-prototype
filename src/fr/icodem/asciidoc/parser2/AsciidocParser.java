package fr.icodem.asciidoc.parser2;

public class AsciidocParser extends Parser {

    private void documentRule() {

    }

    public void document() {
        // document : '=' ' ' [a-z]
    }

    private void eq() {
        System.out.println("eq()");
    }

    private void blank() {
        System.out.println("blank");
    }

    private void anyChar() {
        System.out.println("anyChar");
    }

}
