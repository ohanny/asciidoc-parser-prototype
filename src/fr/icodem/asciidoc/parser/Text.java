package fr.icodem.asciidoc.parser;

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

}
