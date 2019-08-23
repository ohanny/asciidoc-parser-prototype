package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block.listing.ListingProcessor;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Attribute;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Callout;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.HighlightParameter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.ListingBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.ListingLine;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2.HighlightListener2;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.rules2.HighlightRules2;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;

public class ListingBlockBuilder implements BlockBuilder, TextContainer {

    private BlockBuildState state;
    private AttributeList attributeList;
    private TitleBuilder title;
    private String text;
    private Deque<CalloutBuilder> callouts;

    private ListingProcessor listingProcessor; // TODO to be refactored

    public static ListingBlockBuilder newBuilder(BlockBuildState state) {
        ListingBlockBuilder builder = new ListingBlockBuilder();
        builder.state = state;
        builder.attributeList = state.consumeAttributeList();
        builder.title = state.consumeBlockTitle();

        builder.listingProcessor = ListingProcessor.newInstance();

        return builder;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public ListingBlock build() {
        List<Callout> callouts = this.callouts == null?null:this.callouts
                .stream()
                .map(CalloutBuilder::build)
                .collect(Collectors.toList());

        List<ListingLine> lines = buildLines(text.toCharArray());

        return ListingBlock.of(attributeList, buildTitle(title), Text.of(text), callouts, lines);
    }

    public void newCallouts() {
        callouts = new LinkedList<>();
    }

    public CalloutBuilder addCallout() {
        CalloutBuilder builder = CalloutBuilder.newBuilder();
        callouts.addLast(builder);

        return builder;
    }

    public void setCalloutNumber(String number) {
        callouts.peekLast().setNumber(Integer.parseInt(number));
    }

    private List<ListingLine> buildLines(char[] chars) {
        class HighlightParamsHolder {
            List<HighlightParameter> highlightParams = null;

        }
        HighlightParamsHolder paramsHolder = new HighlightParamsHolder();

        AttributeList attList = attributeList;
        if (attList != null) {
            Attribute attHighlightParams = attList.getAttribute("highlight");
            if (attHighlightParams != null) {
                HighlightRules2 rules = new HighlightRules2(state.getAttributeEntries());
                rules.withFactory(defaultRulesFactory());
                ParsingResult result = new ParseRunner(rules, rules::highlights)
                        //.trace()
                        .parse(new StringReader(attHighlightParams.getValue()), new HighlightListener2(params -> paramsHolder.highlightParams = params), null, null);

            }
        }


        return listingProcessor.process(chars, paramsHolder.highlightParams);
    }

}
