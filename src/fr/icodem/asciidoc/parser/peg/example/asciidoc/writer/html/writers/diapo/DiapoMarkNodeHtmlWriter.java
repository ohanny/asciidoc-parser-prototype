package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.DecoratorNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.DecoratorNodeHtmlWriter;

import java.util.stream.Collectors;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.MARK;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.SPAN;

public class DiapoMarkNodeHtmlWriter extends DecoratorNodeHtmlWriter {
    public DiapoMarkNodeHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startNode(DecoratorNode node) {
        AttributeList attList = node.getAttributes();
        if (attList == null || (attList.getFirstPositionalAttribute() == null && attList.getRoles().isEmpty())) {
            append(MARK.start());
        } else {
            String classAtt = attList.getFirstPositionalAttribute();
            if (classAtt == null) {
                classAtt = attList.getRoles()
                        .stream()
                        .collect(Collectors.joining(" "));
            }
            append(SPAN.start("class", classAtt));
        }

    }

    @Override
    protected void endNode(DecoratorNode node) {
        AttributeList attList = node.getAttributes();
        if (attList == null || (attList.getFirstPositionalAttribute() == null  && attList.getRoles().isEmpty())) {
            append(MARK.end());
        } else {
            append(SPAN.end());
        }

    }

}
