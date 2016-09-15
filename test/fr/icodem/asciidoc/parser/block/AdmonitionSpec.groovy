package fr.icodem.asciidoc.parser.block

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class AdmonitionSpec extends BlockBaseSpecification {

    def "note admonition"() {
        given:
        String input = "NOTE: Run `git difftool --tool-help` to see what is available on your system.";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(document (content (block (paragraph (admonition N O T E :  ) R u n   ` g i t   d i f f t o o l   - - t o o l - h e l p `   t o   s e e   w h a t   i s   a v a i l a b l e   o n   y o u r   s y s t e m . <EOF>))))";
    }

}