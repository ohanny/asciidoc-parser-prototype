package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.VideoBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class VideoBlockHtmlWriter extends ModelHtmlWriter<VideoBlockHtmlWriter> {

    public VideoBlockHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public abstract void write(VideoBlock image);
}
