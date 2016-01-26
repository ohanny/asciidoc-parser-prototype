package fr.icodem.asciidoc.parser2;

import java.io.IOException;
import java.io.Reader;

public class AsciidocParser2 {

    // document : '=' ' ' title ;
    // title : [a-z]* ;

    private ParserReader reader;

    public AsciidocParser2(Reader reader) throws IOException {
        this.reader = new ParserReader(reader);
    }

    public void document() {
        reader.mark();

        int c = reader.read();
        match("=", c);
        c = reader.read();
        match(" ", c);
        String title = title();

        System.out.println("TITLE => " + title);
    }

    public String title() {
        //reader.mark();

        int from = reader.getIndex();

        int c;
        do {
            c = reader.read();
            match("[a-z]", c);
        } while (c != -1);

        int to = reader.getIndex();

        String text = reader.getText(from, to);

        return text;
    }

    private void match(String pattern, int c) {
//        char[] az = new char[100];
//        for (char cc = 'a'; cc < 'z'; cc++) {
//            //az[]
//        }

        if (c != -1) {
            if (pattern.equals("[a-z]")) {
                if (c >= 'a' && c <= 'z') {
                    // matched
                }
            } else if (pattern.equals("=")) {
                if (c == '=') {
                    // matched
                }
            }

            //Pattern p = Pattern.compile(pattern);
            //Matcher matcher = p.matcher("");

        }

    }
}
