package fr.icodem.asciidoc.parser.peg.buffers;

public interface BufferLoader<T> {

    int load(T source, char[] buffer, int offset, int length) throws Exception;
}
