package fr.icodem.asciidoc.backend.html;

public class ActionRequest {

    private static int lastId;

    public enum ActionRequestType {
        StartDocument,
        EndDocument,
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
        StartAttributeEntry,
        AttributeList
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

    public void ready() {
        ready = true;
    }

    public int getId() {
        return id;
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
                "id=" + id +
                ", type=" + type +
                ", ready=" + ready +
                '}';
    }
}
