package fr.icodem.asciidoc.parser.peg.buffers;

import fr.icodem.asciidoc.parser.peg.listeners.DefaultInputBufferStateListener;
import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.io.Reader;

public class InputBufferBuilder {

    private int bufferSize;
    private InputBufferStateListener listener;
    private Reader reader;
    private String text;

    private InputBuffer instance;

    private InputBufferBuilder() {}

    public static InputBufferBuilder readerInputBuffer(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }

        InputBufferBuilder builder = new InputBufferBuilder();
        builder.reader = reader;
        builder.bufferSize = 1024;
        builder.instance = new ReaderInputBuffer();
        return builder;
    }

    public static InputBufferBuilder stringInputBuffer(String text) {
        InputBufferBuilder builder = new InputBufferBuilder();
        builder.text = text;
        builder.instance = new StringInputBuffer();
        return builder;
    }


    public InputBufferBuilder bufferSize(int size) {
        this.bufferSize = size;
        return this;
    }

    public InputBufferBuilder useListener(InputBufferStateListener listener) {
        this.listener = listener;
        return this;
    }

    public InputBuffer build() {
        if (instance instanceof ReaderInputBuffer) {
            if (listener == null) listener = new DefaultInputBufferStateListener();
            ((ReaderInputBuffer)instance).init(reader, bufferSize, listener);
        }
        else if (instance instanceof StringInputBuffer) {
            if (listener == null) listener = new DefaultInputBufferStateListener();
            ((StringInputBuffer)instance).init(text, listener);
        }

        return instance;
    }

}
