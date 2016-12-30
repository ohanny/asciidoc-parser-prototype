package fr.icodem.asciidoc.parser.peg;

/**
 * A collection of special characters.
 */
public interface Chars {

    /**
     * The null character
     */
    char NULL = '\u0000';

    /**
     * The End Of Input non-character
     */
    char EOI = '\uFFFF';

    static void append(char c, StringBuilder sb) {
        switch (c) {
            case Chars.EOI:
                sb.append("<EOI>");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\t':
                sb.append("\\t");
                break;
            default:
                sb.append(c);
        }
    }

    static String toString(char... chars) {
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            append(c, sb);
        }
        return sb.toString();
    }

}
