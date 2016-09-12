package fr.icodem.asciidoc.parser.elements;

import java.util.Iterator;
import java.util.function.Consumer;

public interface Text {

    static Text dummy() {
        return DummyText.instance;
    }

    static Text empty() {
        return new DefaultText();
    }

    static Text withValue(String value) {
        Text text = new DefaultText();
        text.offer(value);
        return text;
    }

    static Text formattedText() {
        Text text = new FormattedText();
        return text;
    }

    default String getValue() {
        return null;
    }

    default void offer(String value) {}

    class DefaultText implements Text {
        private String value;

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void offer(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "DefaultText{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    class DummyText implements Text {
        static Text instance = new DummyText();
    }

    class FormattedText implements Text {
        private String rawText;

        private TextItem firstItem;// first item

        public FormattedText() {
        }

        public FormattedTextIterator iterator() {
            return new FormattedTextIterator(this);
        }

        @Override
        public void offer(String value) {
            this.rawText = value;
        }

        @Override
        public String getValue() {
            return rawText;
        }

        public TextItem getFirstItem() {
            return firstItem;
        }

        public void setFirstItem(TextItem firstItem) {
            this.firstItem = firstItem;
        }
    }

    class TextItem {
        private TextItem next;
        protected TextItem head;
        private boolean tail;

        private String text;

        public TextItem getNext() {
            return next;
        }

        public void setNext(TextItem next) {
            this.next = next;
        }

        public TextItem getHead() {
            return head;
        }

        public void setHead(TextItem head) {
            this.head= head;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isTail() {
            return tail;
        }

        public void setTail(boolean tail) {
            this.tail = tail;
        }
    }

    class BoldTextItem extends TextItem {
        private BoldTextItem() {
            setHead(this);
        }

        public static BoldTextItem newInstance() {
            BoldTextItem item = new BoldTextItem();
            return item;
        }
    }

    class ItalicTextItem extends TextItem {
        private ItalicTextItem() {
            setHead(this);
        }

        public static ItalicTextItem newInstance() {
            ItalicTextItem item = new ItalicTextItem();
            return item;
        }
    }
    class FormattedTextIterator implements Iterator<TextItem> {

        //private FormattedText text;
        private TextItem current;

        public FormattedTextIterator(FormattedText text) {
            //this.text = text;
            this.current = new TextItem();
            this.current.setNext(text.getFirstItem());
        }

        @Override
        public boolean hasNext() {
            return current.getNext() != null;
        }

        @Override
        public TextItem next() {
            this.current = current.getNext();
            return current;
        }

        @Deprecated
        public boolean isTail() {
            return true;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer<? super TextItem> action) {
            throw new UnsupportedOperationException();
        }
    }
}
