package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class DeferredHandlerException extends RuntimeException {
    public DeferredHandlerException() {
    }

    public DeferredHandlerException(String message) {
        super(message);
    }

    public DeferredHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeferredHandlerException(Throwable cause) {
        super(cause);
    }

    public DeferredHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
