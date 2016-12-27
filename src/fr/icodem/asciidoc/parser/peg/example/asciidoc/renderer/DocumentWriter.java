package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer;

public  interface DocumentWriter {

    void write(String str);

    void close();

    int getPosition();

    void seek(int position);


    static DocumentWriter bufferedWriter() {
        return new BufferedDocumentWriter();
    }

    static DocumentWriter fileWriter() {
        return null;
    }

    class BufferedDocumentWriter implements DocumentWriter {
        private StringBuilder buffer;

        private int position;

        public BufferedDocumentWriter() {
            this.buffer = new StringBuilder();
            this.position = -1;
        }

        @Override
        public void write(String str) {
            if (position > -1) {
                buffer.insert(position, str);
                position += str.length();
            } else {
                buffer.append(str);
            }
        }

        @Override
        public void close() {}

        @Override
        public int getPosition() {
            return position == -1?buffer.length():position;
        }

        @Override
        public void seek(int position) {
            this.position = position;
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
        public int getPosition() {
            return 0;
        }

        @Override
        public void seek(int position) {

        }

    }
}
