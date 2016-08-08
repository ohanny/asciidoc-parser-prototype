package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.example.Calculator;
import org.junit.Before;
import org.junit.Test;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;
import static org.junit.Assert.assertEquals;

public class CalculatorTest {

    private Calculator calculator;

    @Before
    public void init() {
        calculator = new Calculator();
        calculator.useFactory(defaultRulesFactory());
    }

    @Test
    public void test1() throws Exception {
        int result = calculator.calc("15+2");
        assertEquals("Wrong result", 17, result);
    }

    @Test
    public void test2() throws Exception {
        int result = calculator.calc("15-2");
        assertEquals("Wrong result", 13, result);
    }

    @Test
    public void test3() throws Exception {
        int result = calculator.calc("15*3");
        assertEquals("Wrong result", 45, result);
    }

    @Test
    public void test4() throws Exception {
        int result = calculator.calc("15/3");
        assertEquals("Wrong result", 5, result);
    }

    @Test
    public void test5() throws Exception {
        int result = calculator.calc("12/3+5*40");
        assertEquals("Wrong result", 204, result);
    }

    @Test
    public void test6() throws Exception {
        int result = calculator.calc("12/(1+2)*40");
        assertEquals("Wrong result", 160, result);
    }
}
