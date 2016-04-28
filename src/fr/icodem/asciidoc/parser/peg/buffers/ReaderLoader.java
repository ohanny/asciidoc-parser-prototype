package fr.icodem.asciidoc.parser.peg.buffers;

import java.io.Reader;

public class ReaderLoader implements BufferLoader<Reader> {
    @Override
    public int load(Reader reader, char[] buffer, int offset, int length) throws Exception {
        return reader.read(buffer, offset, length);
    }
}
