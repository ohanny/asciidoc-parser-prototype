package fr.icodem.asciidoc.parser.peg.action;

@FunctionalInterface
public interface Action {
    void execute(ActionContext context);
}
