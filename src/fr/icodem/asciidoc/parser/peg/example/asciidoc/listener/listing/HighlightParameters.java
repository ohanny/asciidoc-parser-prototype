package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import java.util.List;

public class HighlightParameters {
    private List<HighlightParameter> parameters;

    public static HighlightParameters of(List<HighlightParameter> parameters) {
        HighlightParameters params = new HighlightParameters();
        params.parameters = parameters;

        return params;
    }

    public List<HighlightParameter> getParameters() {
        return parameters;
    }

}
