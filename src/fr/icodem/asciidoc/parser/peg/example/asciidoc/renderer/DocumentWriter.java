package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public  interface DocumentWriter {

    void write(String str);

    void close();

    void mark(String marker);

    void seek(String marker);


    static DocumentWriter bufferedWriter() {
        return new BufferedDocumentWriter();
    }

    static DocumentWriter fileWriter() {
        return null;
    }

    class BufferedDocumentWriter implements DocumentWriter {
        private OutputStream os;
        private StringBuilder buffer;

        private Map<String, Integer> markers;
        private int position;

        public BufferedDocumentWriter() {
            this.buffer = new StringBuilder();
            this.markers = new HashMap<>();
            this.position = -1;
        }

        @Override
        public void write(String str) {
            if (position == -1) {
                buffer.append(str);
            } else {
                buffer.insert(position, str);
                for (Map.Entry<String, Integer> entry : markers.entrySet()) {
                    if (entry.getValue() >= position) {
                        entry.setValue(entry.getValue() + str.length());
                    }
                }

                position += str.length();
            }
        }

        @Override
        public void close() {
            try {
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void mark(String marker) {
            markers.put(marker, buffer.length());
        }

        @Override
        public void seek(String marker) {
            this.position = markers.getOrDefault(marker, -1);
        }

        @Override
        public String toString() {
            return buffer.toString();
        }
    }

    class FileDocumentWriter implements DocumentWriter {

        @Override
        public void write(String str) {

        }

        @Override
        public void close() {

        }

        @Override
        public void mark(String marker) {

        }

        @Override
        public void seek(String marker) {

        }

//        @Override
//        public void flush() {
//
//        }
    }
}
