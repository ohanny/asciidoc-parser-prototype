package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

public abstract class TextBlock extends Block {
    protected Text text;

    public Text getText() {
        return text;
    }
}
