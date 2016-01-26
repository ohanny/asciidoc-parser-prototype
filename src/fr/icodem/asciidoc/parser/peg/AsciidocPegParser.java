package fr.icodem.asciidoc.parser.peg;

public class AsciidocPegParser extends BaseParser {

    public static void main(String[] args) throws InterruptedException {
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

    /*

    private Map<String, Rule> nameToRuleCache = new HashMap<>();
    public Rule toCache(Rule rule) {
        nameToRuleCache.put(rule.getName(), rule);
        return rule;
    }

    public Rule fromCache(String name) {
        System.out.println("YYYYY => " + nameToRuleCache.get(name));
        return nameToRuleCache.get(name);
    }

    public Rule named(String name, Rule delegate) {
        return toCache(new NamedRule(name, delegate));
    }

    public Rule node(String name, Rule delegate) {
        return toCache(new NodeRule(name, delegate));
    }

    public Rule proxy(String name) {
        return new ProxyRule(name, nameToRuleCache);
    }

    */


//    public Matcher expression() {
//        Matcher matcher = sequence(ch('a'), zeroOrMore(expression()), ch('b'));
//
//        return matcher;
//    }

//    public Rule ch(char c) {
//        String name = "CharRule." + c;
//        Rule rule = null;//fromCache(name); ?????????
//        if (rule == null) {
//            //rule = toCache(new NamedRule(name, () -> new CharMatcher(c)));
//            rule = named(name, () -> new CharMatcher(c));
//        }
//        return rule;
//    }
//
//    public Rule sequence(Rule... rules) {
//        return () -> new SequenceMatcher(rules);
////        return () -> {
////            Matcher[] matchers = new Matcher[rules.length];
////            Arrays.stream(rules)
////                  .map(r -> r.supply())
////                  .collect(Collectors.toList())
////                  .toArray(matchers);
////            return sequence(matchers);
////        };
//    }
//
//    public Rule zeroOrMore(Rule rule) {
//        System.out.println("ZEROORMORE ");
//
//
//        return () -> new ZeroOrMoreMatcher(rule);
//    }

//    public Rule zeroOrMore(Rule rule) {
//        return () -> {
//            //return zeroOrMore(rule.supply());
//            return zeroOrMore(rule);
//        };
//    }
//

    // OLD
//    public Matcher sequence(Matcher... rules) {
//        return new SequenceMatcher(rules);
//    }
//
//    public Matcher ch(char c) {
//        return new CharMatcher(c);
//    }
//
//    public Matcher firstOf(Matcher... matchers) {
//        return new FirstOfMatcher(matchers);
//    }
//
//    public Matcher zeroOrMore(Matcher matcher) {
//        return new ZeroOrMoreMatcher(matcher);
//    }
//
//    public Rule call(Rule rule) {
//        return () -> new ProxyMatcher(rule);
//    }



    //*******************************

//    public Rule ch(char c) {
//        return null;
//    }

//    public Rule charRange(char cLow, char cHigh) {
//        //return cLow == cHigh ? Ch(cLow) : new CharRangeMatcher(cLow, cHigh);
//        return null;
//    }
//
//    public Rule anyOf(String characters) {
//        return anyOf(characters.toCharArray());
//    }
//
//    public Rule anyOf(char[] characters) {
//        //return characters.length == 1 ? ch(characters[0]) : AnyOf(Characters.of(characters));
//        return null;
//    }

//    public Rule AnyOf(Characters characters) {
//        if (!characters.isSubtractive() && characters.getChars().length == 1) {
//            return Ch(characters.getChars()[0]);
//        }
//        if (characters.equals(Characters.NONE)) return NOTHING;
//        return new AnyOfMatcher(characters);
//    }

//    public Rule noneOf(String characters) {
//        //return NoneOf(characters.toCharArray());
//        return null;
//    }

//    public Rule NoneOf(char[] characters) {
//        checkArgNotNull(characters, "characters");
//        checkArgument(characters.length > 0);
//
//        // make sure to always exclude EOI as well
//        boolean containsEOI = false;
//        for (char c : characters) if (c == Chars.EOI) { containsEOI = true; break; }
//        if (!containsEOI) {
//            char[] withEOI = new char[characters.length + 1];
//            System.arraycopy(characters, 0, withEOI, 0, characters.length);
//            withEOI[characters.length] = Chars.EOI;
//            characters = withEOI;
//        }
//
//        return AnyOf(Characters.allBut(characters));
//    }

//    public Rule String(char... characters) {
////        if (characters.length == 1) return ch(characters[0]); // optimize one-char strings
////        Rule[] matchers = new Rule[characters.length];
////        for (int i = 0; i < characters.length; i++) {
////            matchers[i] = ch(characters[i]);
////        }
//        //return new StringMatcher(matchers, characters);
//        return null;
//    }
//
//    public Rule ignoreCase(char c) {
////        if (Character.isLowerCase(c) == Character.isUpperCase(c)) {
////            return Ch(c);
////        }
////        return new CharIgnoreCaseMatcher(c);
//        return null;
//    }

//    public Rule firstOf(Rule... rules) {
//        return null;
//    }
//
//    public Rule OneOrMore(Object rule) {
//        //return new OneOrMoreMatcher(toRule(rule));
//        return null;
//    }
//
//    public Rule optional(Rule rule) {
//        return null;
//    }
//
//    public Rule optional(Rule... rules) {
//        return optional(sequence(rules));
//    }
//
//
//    public Rule sequence(Rule... rules) {
//        return null;
//    }
//
//    public Rule Test(Object rule) {
//        //Rule subMatcher = toRule(rule);
//        //return new TestMatcher(subMatcher);
//        return null;
//    }
//
//    public Rule TestNot(Object rule) {
//        //Rule subMatcher = toRule(rule);
//        //return new TestNotMatcher(subMatcher);
//        return null;
//    }
//
//    public Rule ZeroOrMore(Object rule) {
//        //return new ZeroOrMoreMatcher(toRule(rule));
//        return null;
//    }
//
//    public Rule NTimes(int repetitions, Object rule) {
//        return null;
//        //return NTimes(repetitions, rule, null);
//    }


}
