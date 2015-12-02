package fr.icodem.asciidoc.parser;

public class ActionRequest {

    public enum ActionRequestType {
        StartDocument,
        EndDocument,
        DocumentHeader,
        StartPreamble,
        EndPreamble,
        StartParagraph,
        StartSection,
        StartSectionTitle,
        StartAttributeEntry
    }

    private ActionRequestType type;
    private Runnable action;
    private boolean ready;

    public ActionRequest(ActionRequestType type, Runnable action, boolean ready) {
        this.type = type;
        this.action = action;
        this.ready = ready;
    }

    public void ready() {// TODO to be removed ?
        ready = true;
    }

    public void ready(Runnable action) {
        this.action = action;
        ready = true;
    }

    public ActionRequestType getType() {
        return type;
    }

    public Runnable getAction() {
        return action;
    }

    public boolean isReady() {
        return ready;
    }

    @Override
    public String toString() {
        return "ActionRequest{" +
                ", type=" + type +
                ", ready=" + ready +
                '}';
    }
}
