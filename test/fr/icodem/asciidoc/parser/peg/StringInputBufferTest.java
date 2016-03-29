package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.listeners.DefaultInputBufferStateListener;
import org.junit.Test;

import static fr.icodem.asciidoc.parser.peg.Chars.*;
import static org.junit.Assert.*;

public class StringInputBufferTest {

    private InputBuffer buffer;

    @Test
    public void itShouldReadAllCharacters() throws Exception {
        buffer = InputBuffer.stringInputBuffer("abcde", new DefaultInputBufferStateListener());

        final char nextChar1 = buffer.getNextChar();
        final char nextChar2 = buffer.getNextChar();
        final char nextChar3 = buffer.getNextChar();
        final char nextChar4 = buffer.getNextChar();
        final char nextChar5 = buffer.getNextChar();
        final char nextChar6 = buffer.getNextChar();
        final char nextChar7 = buffer.getNextChar();

        assertEquals("First char is wrong", 'a', nextChar1);
        assertEquals("Second char is wrong", 'b', nextChar2);
        assertEquals("Third char is wrong", 'c', nextChar3);
        assertEquals("Fourth char is wrong", 'd', nextChar4);
        assertEquals("Fifth char is wrong", 'e', nextChar5);
        assertEquals("Sixth char is wrong", EOI, nextChar6);
        assertEquals("Seventh char is wrong", EOI, nextChar7);
    }

    @Test
    public void itShouldGetMarkers() throws Exception {
        buffer = InputBuffer.stringInputBuffer("abcde", new DefaultInputBufferStateListener());

        final char nextChar1 = buffer.getNextChar();
        final char nextChar2 = buffer.getNextChar();
        final char nextChar3 = buffer.getNextChar();
        int marker1 = buffer.getPosition();

        final char nextChar4 = buffer.getNextChar();
        int marker2 = buffer.getPosition();

        final char nextChar5 = buffer.getNextChar();
        final char nextChar6 = buffer.getNextChar();
        final char nextChar7 = buffer.getNextChar();

        assertEquals("First char is wrong", 'a', nextChar1);
        assertEquals("Second char is wrong", 'b', nextChar2);
        assertEquals("Third char is wrong", 'c', nextChar3);
        assertEquals("First marker is wrong", 2, marker1);

        assertEquals("Fourth char is wrong", 'd', nextChar4);
        assertEquals("Second marker is wrong", 3, marker2);

        assertEquals("Fifth char is wrong", 'e', nextChar5);
        assertEquals("Sixth char is wrong", EOI, nextChar6);
        assertEquals("Seventh char is wrong", EOI, nextChar7);
    }

    @Test
    public void itShouldResetToMarker() throws Exception {
        buffer = InputBuffer.stringInputBuffer("abcde", new DefaultInputBufferStateListener());

        final char nextChar1 = buffer.getNextChar();
        final char nextChar2 = buffer.getNextChar();
        int marker = buffer.getPosition();

        final char nextChar3 = buffer.getNextChar();
        final char nextChar4 = buffer.getNextChar();
        buffer.reset(marker);

        final char nextChar5 = buffer.getNextChar();
        final char nextChar6 = buffer.getNextChar();

        assertEquals("First char is wrong", 'a', nextChar1);
        assertEquals("Second char is wrong", 'b', nextChar2);
        assertEquals("Third char is wrong", 'c', nextChar3);
        assertEquals("Fourth char is wrong", 'd', nextChar4);

        assertEquals("Fifth char is wrong", 'c', nextChar5);
        assertEquals("Sixth char is wrong", 'd', nextChar6);
    }

    @Test
    public void itShouldConsumeReadCharacters() throws Exception {
        buffer = InputBuffer.stringInputBuffer("abcde", new DefaultInputBufferStateListener());
        buffer.getNextChar();
        buffer.getNextChar();
        buffer.consume();

        final char nextChar1 = buffer.getNextChar();
        final char nextChar2 = buffer.getNextChar();
        final char nextChar3 = buffer.getNextChar();
        final char nextChar4 = buffer.getNextChar();

        assertEquals("First char is wrong", 'c', nextChar1);
        assertEquals("Second char is wrong", 'd', nextChar2);
        assertEquals("Third char is wrong", 'e', nextChar3);
        assertEquals("Fourth char is wrong", EOI, nextChar4);

    }

    @Test
    public void test() throws Exception {
        buffer = new StringInputBuffer("abc\n\n", new DefaultInputBufferStateListener());


        int pos0 = buffer.getPositionInLine();

        char char1 = buffer.getNextChar();
        int pos1 = buffer.getPositionInLine();

        char char2 = buffer.getNextChar();
        int pos2 = buffer.getPositionInLine();

        char char3 = buffer.getNextChar();
        int pos3 = buffer.getPositionInLine();

        char char4 = buffer.getNextChar();
        int pos4 = buffer.getPositionInLine();

        char char5 = buffer.getNextChar();
        int pos5 = buffer.getPositionInLine();

        assertEquals("Position 0 is wrong", -1, pos0);

        assertEquals("Char 1 is wrong", 'a', char1);
        assertEquals("Position 1 is wrong", 0, pos1);

        assertEquals("Char 2 is wrong", 'b', char2);
        assertEquals("Position 2 is wrong", 1, pos2);

        assertEquals("Char 3 is wrong", 'c', char3);
        assertEquals("Position 3 is wrong", 2, pos3);

        assertEquals("Char 4 is wrong", '\n', char4);
        assertEquals("Position 4 is wrong", 3, pos4);

        assertEquals("Char 5 is wrong", '\n', char5);
        assertEquals("Position 5 is wrong", 0, pos5);

    }
}
