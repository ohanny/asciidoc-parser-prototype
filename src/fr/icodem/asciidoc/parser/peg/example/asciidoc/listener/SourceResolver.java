package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

@FunctionalInterface
public interface SourceResolver {
    Reader resolve(String name) ;

    static SourceResolver defaultResolver() {
        SourceResolver resolver = name -> {
            try {
                return new FileReader(name);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return new StringReader("");
        };
        return resolver;
    }
}
