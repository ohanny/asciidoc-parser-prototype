package fr.icodem.asciidoc.parser;

public class ActionRequest {//TODO change package

    private static int lastId;// TODO to be removed

    public enum ActionRequestType {
        StartDocument,
        EndDocument,
        DocumentHeader,
        StartDocumentTitle,
        EndDocumentTitle,
        StartPreamble,
        EndPreamble,
        StartTitle,
        StartParagraph,
        StartSection,
        EndSection,
        StartSectionTitle,
        //EndSectionTitle
        StartAttributeEntry
    }

    private int id;
    private ActionRequestType type;
    private Runnable action;
    private boolean ready;

    public ActionRequest(ActionRequestType type, Runnable action, boolean ready) {
        this.type = type;
        this.action = action;
        this.ready = ready;

        this.id = ++lastId;
    }

    public void ready() {// TODO to be removed ?
        ready = true;
    }

    public void ready(Runnable action) {
        this.action = action;
        ready = true;
    }

    public int getId() {
        return id;
    }// TODO not used : to be removed ?

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
                "id=" + id +
                ", type=" + type +
                ", ready=" + ready +
                '}';
    }
}
