package fr.icodem.asciidoc.renderer;

import fr.icodem.asciidoc.renderer.html.block.HorizontalRuleSpec;
import fr.icodem.asciidoc.renderer.html.block.ParagraphSpec;
import fr.icodem.asciidoc.renderer.html.list.OrderedListSpec;
import fr.icodem.asciidoc.renderer.html.list.UnorderedListSpec;
import fr.icodem.asciidoc.renderer.html.section.HeadingsSpec;
import fr.icodem.asciidoc.renderer.html.table.TableSpec;
import fr.icodem.asciidoc.renderer.html.text.FormattedTextSpec;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        HeadingsSpec.class,
        HorizontalRuleSpec.class,
        UnorderedListSpec.class,
        OrderedListSpec.class,
        FormattedTextSpec.class,
        ParagraphSpec.class,
        TextOutputterSpec.class,
        TableSpec.class
})
public class RendererTestSuite {
}
