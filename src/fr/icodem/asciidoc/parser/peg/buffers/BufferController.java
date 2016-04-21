package fr.icodem.asciidoc.parser.peg.buffers;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class BufferController<T> {
    private SpaceInfo active;
    private SpaceInfo free;
    private SpaceInfo suspended;

    private char[] data;
    //private int numberOfCharacters;

    // temp variables
    private int freeSize;

    private Deque<SourceContext<T>> sources = new LinkedList<>();

    public void initBuffer(char[] data) {
        this.data = data;
    }

    // remaining data to read - ensure capacity
    public char[] ensureCapacity(InputBufferStateListener listener, int position, int offset, int numberOfCharacters) {// TODO temp args
        int free = data.length - numberOfCharacters;
        if (free == 0) {
            data = Arrays.copyOf(data, data.length * 2);
            free = data.length - numberOfCharacters;
            listener.visitData("increase", data, numberOfCharacters, position, offset);
        }
        freeSize = free;
        return data;
    }

    public T getCurrentSource() {
        SourceContext<T> actualSource = sources.getLast();
        return actualSource.getSource();
    }

    public void include(T source, int actualPos) {

        // split actual source data
        SourceContext<T> actualSource = sources.pollLast();
        if (actualSource != null) {
            //actualSource.suspend(pos);
        }


        // append new source data in active space
        SourceContext<T> sourceCtx = new SourceContext<>(source);
        sourceCtx.addSegment(actualPos + 1);

        sources.offer(sourceCtx);


        //active.append(source);

    }

    public void suspend() {

    }

    public void restore() {

    }

    public int getFreeSize() {
        return freeSize;
    }
}
