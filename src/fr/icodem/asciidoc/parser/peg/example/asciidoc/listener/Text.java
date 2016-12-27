package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public interface Text {

    static Text dummy() {
        return DummyText.instance;
    }

    static Text empty() {
        return new DefaultText();
    }

    static Text of(String value) {
        Text text = new DefaultText();
        text.setValue(value);
        return text;
    }

    default String getValue() {
        return null;
    }

    default void setValue(String value) {}

    class DefaultText implements Text {
        private String value;

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(String value) {
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
