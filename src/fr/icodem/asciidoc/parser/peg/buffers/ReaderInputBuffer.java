package fr.icodem.asciidoc.parser.peg.buffers;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.io.IOException;
import java.io.Reader;

/**
 * A buffer holding the input text to be parsed.
 */
@Deprecated
public class ReaderInputBuffer implements InputBuffer<Reader> {

    private BufferController<Reader> bufferController;

    ReaderInputBuffer() {}

    void init(Reader reader, int bufferSize, InputBufferStateListener listener) {
        BufferLoader<Reader> loader = (reader1, buffer, offset1, length1) -> reader1.read(buffer, offset1, length1);
        //bufferController = new BufferController((reader1, buffer, offset, length) -> reader1.read(buffer, offset, length))
        bufferController = new BufferController(loader)
                .initBuffer(bufferSize)
                .useListener(listener);

        bufferController.include(reader);
    }

    @Override
    public char getNextChar() {
        return bufferController.getNextChar();
    }

    @Override
    public int getPositionInLine() {
        return bufferController.getPositionInLine();
    }

    @Override
    public int getPosition() {
        return bufferController.getPosition();
    }

    @Override
    public char[] extract(int start, int end) {
        return bufferController.extract(start, end);
    }

    @Override
    public void reset(int marker) {
        bufferController.reset(marker);
    }

    @Override
    public void consume() {
        bufferController.consume();
    }

    @Override
    public ReaderInputBuffer include(Reader reader) {
        bufferController.include(reader);
        return this;
    }

}
