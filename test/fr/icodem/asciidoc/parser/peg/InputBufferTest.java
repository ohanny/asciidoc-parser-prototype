package fr.icodem.asciidoc.parser.peg;

import org.junit.Test;

import static fr.icodem.asciidoc.parser.peg.Chars.*;
import static org.junit.Assert.*;

public class InputBufferTest {

    private InputBuffer buffer;

    @Test
    public void itShouldReadAllCharacters() throws Exception {
        buffer = new InputBuffer("abcde");

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
        buffer = new InputBuffer("abcde");

        final char nextChar1 = buffer.getNextChar();
        final char nextChar2 = buffer.getNextChar();
        final char nextChar3 = buffer.getNextChar();
        int marker1 = buffer.mark();

        final char nextChar4 = buffer.getNextChar();
        int marker2 = buffer.mark();

        final char nextChar5 = buffer.getNextChar();
        final char nextChar6 = buffer.getNextChar();
        final char nextChar7 = buffer.getNextChar();

        assertEquals("First char is wrong", 'a', nextChar1);
        assertEquals("Second char is wrong", 'b', nextChar2);
        assertEquals("Third char is wrong", 'c', nextChar3);
        assertEquals("First marker is wrong", 0, marker1);

        assertEquals("Fourth char is wrong", 'd', nextChar4);
        assertEquals("First marker is wrong", 1, marker2);

        assertEquals("Fifth char is wrong", 'e', nextChar5);
        assertEquals("Sixth char is wrong", EOI, nextChar6);
        assertEquals("Seventh char is wrong", EOI, nextChar7);
    }

    @Test
    public void itShouldResetToMarker() throws Exception {
        buffer = new InputBuffer("abcde");

        final char nextChar1 = buffer.getNextChar();
        final char nextChar2 = buffer.getNextChar();
        int marker = buffer.mark();

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

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowExceptionWhenMarkerResetIsWrong() throws Exception {
        buffer = new InputBuffer("abcde");

        buffer.getNextChar();
        buffer.getNextChar();
        buffer.mark();
        buffer.reset(99);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowExceptionWhenResettingAClearedMarker() throws Exception {
        buffer = new InputBuffer("abcde");

        buffer.getNextChar();
        buffer.getNextChar();
        int marker1 = buffer.mark();
        buffer.getNextChar();
        int marker2 = buffer.mark();
        buffer.reset(marker1);
        buffer.reset(marker2);
    }

    @Test
    public void itShouldConsumeReadCharacters() throws Exception {
        buffer = new InputBuffer("abcde");
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
}
