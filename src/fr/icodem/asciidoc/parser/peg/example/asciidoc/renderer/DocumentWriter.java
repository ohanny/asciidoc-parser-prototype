package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public interface DocumentWriter {

    void write(String str);

    void close();

    int getPosition();

    void seek(int position);

    void endInsert();


    static DocumentWriter bufferedWriter() {
        return new BufferedDocumentWriter();
    }

    static DocumentWriter fileWriter(String filename) {
        return new FileDocumentWriter(filename);
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
        public void endInsert() {
            position = -1;
        }

        @Override
        public String toString() {
            return buffer.toString();
        }
    }

    class FileDocumentWriter implements DocumentWriter {

        private RandomAccessFile target;
        private RandomAccessFile temp;
        private FileChannel targetChannel;
        private FileChannel tempChannel;
        private File targetFile;
        private File tempFile;
        private long tempSize;

        public FileDocumentWriter(String filename) {
            try {
                this.tempSize = -1;

                targetFile = new File(filename);
                tempFile = new File(filename + "~");

                if (targetFile.exists()) targetFile.delete();
                if (tempFile.exists()) tempFile.delete();

                target = new RandomAccessFile(targetFile, "rw");
                temp = new RandomAccessFile(tempFile, "rw");

                targetChannel = target.getChannel();
                tempChannel = temp.getChannel();
            } catch (IOException e) {
                throw new RendererException("Failed opening file : " + filename, e);
            }
        }

        @Override
        public void write(String str) {
            try {
                target.write(str.getBytes(Charset.defaultCharset())); // TODO set charset
            } catch (IOException e) {
                throw new RendererException("Failed writing to file", e);
            }
        }

        @Override
        public void close() {
            try {
                targetChannel.close();
                tempChannel.close();
                target.close();
                temp.close();
                tempFile.delete();
            } catch (IOException e) {
                throw new RendererException("Failed closing file", e);
            }
        }

        @Override
        public int getPosition() {
            try {
                return (int)targetChannel.position();// TODO check position type long
            } catch (IOException e) {
                throw new RendererException("Failed getting position in channel", e);
            }
        }

        @Override
        public void seek(int position) {
            try {
                long targetSize = target.length();
                tempSize = targetSize - position;
                targetChannel.transferTo(position, tempSize, tempChannel);
                targetChannel.truncate(position);
                target.seek(position);
            } catch (IOException e) {
                throw new RendererException("Failed positionning pointer in file", e);
            }
        }

        @Override
        public void endInsert() {
            try {
                tempChannel.position(0L);
                long position = target.getFilePointer();
                targetChannel.transferFrom(tempChannel, position, tempSize);
                this.tempSize = -1;
            } catch (IOException e) {
                throw new RendererException("Failed restoring temp data", e);
            }
        }
    }
}
