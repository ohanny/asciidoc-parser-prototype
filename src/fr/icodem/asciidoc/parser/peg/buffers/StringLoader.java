package fr.icodem.asciidoc.parser.peg.buffers;

public class StringLoader implements BufferLoader<StringHolder> {

    @Override
    public int load(StringHolder source, char[] buffer, int offset, int length) throws Exception {
        if (source.getCharsLoaded() == source.length()) return -1;

        int charsToLoad = Math.min(source.length() - source.getCharsLoaded(), length);
        for (int i = 0; i < charsToLoad; i++) {
            buffer[i + offset] = source.charAt(i + source.getCharsLoaded());
        }
        source.increment(charsToLoad);
        return charsToLoad;
    }
}
