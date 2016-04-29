package fr.icodem.asciidoc.parser.peg.buffers;

import fr.icodem.asciidoc.parser.peg.listeners.DefaultInputBufferStateListener;
import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.io.Reader;

import static fr.icodem.asciidoc.parser.peg.buffers.InputBufferBuilder.BufferType.*;
import static fr.icodem.asciidoc.parser.peg.buffers.InputBufferBuilder.BufferType.String;

public class InputBufferBuilder {

    public enum BufferType {String, Reader}

    private int bufferSize;
    private InputBufferStateListener listener;
    private Reader reader;
    private String text;

    private BufferType type;

    private InputBufferBuilder() {}

    public static InputBufferBuilder readerInputBuffer(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }

        InputBufferBuilder builder = new InputBufferBuilder();
        builder.reader = reader;
        builder.bufferSize = 1024;
        builder.type = Reader;
        return builder;
    }

    public static InputBufferBuilder stringInputBuffer(String text) {
        InputBufferBuilder builder = new InputBufferBuilder();
        builder.text = text;
        builder.type = String;
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
        if (Reader.equals(type)) {
            if (listener == null) listener = new DefaultInputBufferStateListener();
            BufferController<Reader> inputBuffer =
                    new BufferController<>(new ReaderLoader())
                        .initBuffer(bufferSize)
                        .useListener(listener)
                        .include(reader);

            return inputBuffer;
        }
        else if (String.equals(type)) {
            if (listener == null) listener = new DefaultInputBufferStateListener();

            BufferController<StringHolder> inputBuffer =
                    new BufferController<>(new StringLoader())
                            .initBuffer(text.length() + 1) // last pos for EOI
                            .useListener(listener)
                            .include(new StringHolder(text));

            return inputBuffer;
        }

        return null;
    }

}
