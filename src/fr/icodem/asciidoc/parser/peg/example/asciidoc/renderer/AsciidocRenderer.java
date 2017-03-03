package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer;

import java.io.File;

public interface AsciidocRenderer {

    void render(String source);
    void render(File source);
}
