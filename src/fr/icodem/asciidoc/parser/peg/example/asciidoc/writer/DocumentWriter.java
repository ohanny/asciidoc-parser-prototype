package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer;

import java.io.*;
import java.nio.file.Path;

public interface DocumentWriter {
    void write(Reader reader, Writer writer) throws IOException;

    default void write(Path input, Path output) throws IOException {
        write(input.toFile(), output.toFile());
    }

    default void write(File input, File output) throws IOException {
        try (
          Reader reader = new FileReader(input);
          Writer writer = new FileWriter(output)
        ) {
            write(reader, writer);
        }
    }

    default void write(String input, OutputStream output) throws IOException {
        try (
          Reader reader = new StringReader(input);
          Writer writer = new OutputStreamWriter(output)
        ) {
            write(reader, writer);
        }
    }
}
