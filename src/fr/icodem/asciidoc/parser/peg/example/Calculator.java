package fr.icodem.asciidoc.parser.peg.example;

import fr.icodem.asciidoc.parser.peg.BaseRules;
import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

import java.util.Deque;
import java.util.LinkedList;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;

public class Calculator extends BaseRules implements ParseTreeListener {

    private Deque<Integer> stack = new LinkedList<>();
    private String currentText;

    public static void main(String[] args) {

        Calculator calculator = new Calculator();
        calculator.withFactory(defaultRulesFactory());

        calculator.print("15+2");
        calculator.print("15-2");
        calculator.print("15*3");
        calculator.print("15/3");
        calculator.print("12/3+5*40");
        calculator.print("12/(1+2)*40");

    }

    public void print(String line) {
        try {
            System.out.println("\r\n" + line + " = " + calc(line) + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int calc(String line) {
        ParseRunner runner = new ParseRunner(this, this::inputLine);
        ParsingResult result = runner.parse(line, this);

        // uncomment to get more detail about processing
        //ParsingResult result = runner.trace().parse(line, this);

        if (!result.matched) {
            throw new IllegalArgumentException("Wrong input line");
        }

        return stack.peek();
    }


    // parse tree listener
    @Override
    public void characters(NodeContext context, char[] chars, int startIndex, int endIndex) {
        currentText = new String(chars);
    }

    @Override
    public void enterNode(NodeContext context) {}

    @Override
    public void exitNode(NodeContext context) {
        switch (context.getNodeName()) {
            case "number":
                stack.push(Integer.parseInt(currentText));
                break;

            case "plus":
                stack.push(stack.pop() + stack.pop());
                break;

            case "minus":
                stack.push(-stack.pop() + stack.pop());
                break;

            case "mult":
                stack.push(stack.pop() * stack.pop());
                break;

            case "div":
                int value = stack.pop();
                value = stack.pop() / value;
                stack.push(value);
                break;
        }

    }


    // rules
    Rule inputLine() {
        return node("inputLine", sequence(expression(), eoi()));
    }

    Rule expression() {
        return node("expression", sequence(
                                      term(),
                                      zeroOrMore(
                                          firstOf(
                                              node("plus", sequence(ch('+'), term())),
                                              node("minus", sequence(ch('-'), term()))
                                          )
                                      )
                                   )
        );
    }


    Rule term() {
        return node("term", sequence(
                                factor(),
                                zeroOrMore(
                                    firstOf(
                                        node("mult", sequence(ch('*'), factor())),
                                        node("div", sequence(ch('/'), factor()))
                                    )
                                )
                            )
        );
    }

    Rule factor() {
        return node("factor", firstOf(number(), parens()));
    }

    Rule parens() {
        return node("parens", sequence(ch('('), proxy("expression"), ch(')')));
    }

    Rule number() {
        return node("number", oneOrMore(charRange('0', '9')));
    }

}
