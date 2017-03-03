package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;

@FunctionalInterface
public interface SourceResolver {
    Reader resolve(String name) ;

    static SourceResolver defaultResolver(AttributeEntries attributeEntries) {
        SourceResolver resolver = name -> {
            try {
                Path path = Paths.get(attributeEntries.getValue("docdir"))
                                    .resolve(name);
                return new FileReader(path.toFile());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return new StringReader("");
        };
        return resolver;
    }
}
