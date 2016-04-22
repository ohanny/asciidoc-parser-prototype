package fr.icodem.asciidoc.parser.peg.buffers;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.io.IOException;
import java.io.Reader;

/**
 * A buffer holding the input text to be parsed.
 */
public class ReaderInputBuffer implements InputBuffer {

    private BufferController<Reader> bufferController;

    ReaderInputBuffer() {}

    void init(Reader reader, int bufferSize, InputBufferStateListener listener) {
        bufferController = new BufferController()
                .initBuffer(bufferSize)
                .useListener(listener)
                .include(reader);
    }

    /**
     * Constructs an input buffer given an input reader.
     * @param reader the input text to be parsed
     */
    /*ReaderInputBuffer(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }

        bufferController = new BufferController()
                .initBuffer(1024)
                .include(reader);
    }*/

//    public ReaderInputBuffer bufferSize(int size) {
//        bufferController.initBuffer(size);
//        return this;
//    }

    /**
     * The listener to be notified of internal state of the buffer
     * @param listener the listener notified of internal state of the buffer
     */
//    @Override
//    public InputBuffer useListener(InputBufferStateListener listener) {
//        bufferController.useListener(listener);
//        return this;
//    }


    @Override
    public char getNextChar() {
        // load chars from reader if needed
        if (bufferController.shouldLoadFromSource()) {
            loadFromSource();
        }

        // get next char from buffer
        return bufferController.getNextChar();
    }

    private void loadFromSource() {
        try {
            char[] data = bufferController.ensureCapacity();

            int offset = bufferController.getFreeSpaceOffset();
            int length = bufferController.getFreeSpaceSize();

            // fill data from input
            final Reader reader = bufferController.getCurrentSource();
            int numRead = reader.read(data, offset, length);

            // new data added to buffer
            if (numRead != -1) {
                bufferController.newDataAddedToBuffer(numRead);
            } else {
                bufferController.endOfInput();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void consume(int limit) {
        bufferController.consume(limit);
    }

    @Override
    public void include(Reader reader) {
        bufferController.include(reader);
    }

}
