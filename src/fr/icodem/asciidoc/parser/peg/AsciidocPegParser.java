package fr.icodem.asciidoc.parser.peg;

public class AsciidocPegParser extends BaseParser {

    public static void main(String[] args) throws InterruptedException {
//        Thread.sleep(30000);

        new AsciidocPegParser().parse("aaaaabbbbb");
    }

    private InputBuffer input;

    public void parse(String text) {
        input = new InputBuffer(text);

        Rule ab = ab();

        Rule expression = expression();
        Matcher matcher = expression.getMatcher();

        boolean result = matcher.match(new MatcherContext(input));

        System.out.println(result);
    }


    public Rule ab() {
        return sequence(a(), b());
    }

    public Rule a() {
        return ch('a');
    }

    public Rule b() {
        return ch('b');
    }

    public Rule expression() {
        //return node("expression", sequence(a(), zeroOrMore(proxy("expression")), b(), proxy("expression")));
        return node("expression", sequence(a(), zeroOrMore(proxy("expression")), b()));
//        return sequence(a(), zeroOrMore(() -> expression().supply()), b());
        //return sequence(a(), zeroOrMore(call(() -> expression().supply())), b());
    }



}
