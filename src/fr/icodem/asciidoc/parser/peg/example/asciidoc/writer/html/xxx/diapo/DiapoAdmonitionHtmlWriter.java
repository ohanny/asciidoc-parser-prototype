package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.AdmonitionHtmlWriter;

import java.util.Properties;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.*;

public class DiapoAdmonitionHtmlWriter extends AdmonitionHtmlWriter {

    private static Properties ADMONITIONS = new Properties();
    {
        ADMONITIONS.setProperty("note", "Note");
        ADMONITIONS.setProperty("tip", "Tip");
        ADMONITIONS.setProperty("important", "Important");
        ADMONITIONS.setProperty("caution", "Caution");
        ADMONITIONS.setProperty("warning", "Warning");
    }

    public DiapoAdmonitionHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startAdmonition(String admonition, Block block) {
        String icons = getAttributeEntry("icons").getValue();
        String classes = getMoreClasses("admonitionblock " + admonition, block.getAttributes());
        String style = styleBuilder().reset(block.getAttributes()).addPosition().addSize().style();

        indent().append(DIV.start("class", classes, "style", style)).nl()
          .incIndent()
            .indent().append(TABLE.start()).nl()
            .incIndent()
              .indent().append(TR.start()).nl()
                .incIndent()
                .indent().append(TD.start("class", "icon")).nl()
                .incIndent()
                  .indent().appendIf("font".equals(icons), () ->
                    append(I.start("class", "fa icon-" + admonition, "title", ADMONITIONS.getProperty(admonition))).append(I.end())
                  )
                  .appendIf(!"font".equals(icons), () ->
                        append(DIV.start("class", "title")).append(ADMONITIONS.getProperty(admonition)).append(DIV.end())
                  ).nl()
                .decIndent()
                .indent().append(TD.end()).nl()
                .indent().append(TD.start("class", "content")).nl()
                .incIndent()
                  .indent()
        ;

    }

    @Override
    protected void endAdmonition(String admonition, Block block) {
        nl()
          .decIndent()
          .indent().append(TD.end()).nl()
          .decIndent()
          .indent().append(TR.end()).nl()
          .decIndent()
          .indent().append(TABLE.end()).nl()
          .decIndent()
          .indent().append(DIV.end()).nl();
    }

}
