package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.VideoBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.VideoBlockHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.DIV;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.VIDEO;

public class DiapoVideoBlockHtmlWriter extends VideoBlockHtmlWriter {

    public DiapoVideoBlockHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    public void write(VideoBlock video) {
        String classes = getMoreClasses("videoblock", video.getAttributes());
        indent().append(DIV.start("class", classes, "style",
                    styleBuilder().reset(video.getAttributes()).addPosition().style())).nl()
          .incIndent()
            .indent().append(DIV.start("class", "content")).nl()
            .incIndent()
              .indent().append(VIDEO.start("src", video.getSource(), "controls", "true", "style",
                  styleBuilder().reset(video.getAttributes()).addSize().style())).nl()
              .incIndent()
                .indent().append("Your browser does not support the video tag.").nl()
              .decIndent()
              .indent().append(VIDEO.end()).nl()
            .decIndent()
            .indent().append(DIV.end()).nl()
          .decIndent()
          .indent().append(DIV.end()).nl()
        ;
    }

}
