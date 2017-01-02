package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.shower;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.DocumentWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html.DefaultHtmlRenderer;

public class ShowerRenderer extends DefaultHtmlRenderer {
    private ShowerRenderer(DocumentWriter writer) {
        super(writer);
    }

    /*
    <head>
	<title>Shower Presentation Engine</title>
	<meta charset="utf-8">
	<meta http-equiv="x-ua-compatible" content="ie=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="shower/themes/ribbon/styles/screen-16x10.css">
</head>

     */

    @Override
    public void startDocument() {
        super.startDocument();
    }
}
