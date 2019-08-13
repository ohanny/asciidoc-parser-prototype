package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html;

import java.io.PrintWriter;
import java.io.Writer;

public class Outputter {
    private PrintWriter writer;

    public Outputter(Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    public void print(String str) {
        writer.print(str);
    }
}
