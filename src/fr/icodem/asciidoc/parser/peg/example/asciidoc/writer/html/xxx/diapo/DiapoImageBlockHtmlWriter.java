package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ImageBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Title;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.ImageBlockHtmlWriter;

import java.io.IOException;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.*;

public class DiapoImageBlockHtmlWriter extends ImageBlockHtmlWriter {

    public DiapoImageBlockHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    public void write(ImageBlock image) throws IOException {
        String classes = getCssClasses(image.getAttributes());
        indent().append(FIGURE.start("class", classes, "style",
                    styleBuilder().reset(image.getAttributes()).addPosition().style())).nl()
                .incIndent()
                  .indent().append(IMG.tag("src", image.getSource(), "alt", image.getAlternateText(),
                        "style", styleBuilder().reset(image.getAttributes()).addSize().style())).nl()
                .appendIf(image.getTitle() != null, () ->
                  indent().append(FIGCAPTION.start()).nl()
                    .incIndent()
                      .append(() -> getBlockTitleWriter().write(image.getTitle()))
                    .decIndent()
                    .indent().append(FIGCAPTION.end()).nl()
                )
                .decIndent()
                .indent().append(FIGURE.end()).nl()
        ;

    }

}
