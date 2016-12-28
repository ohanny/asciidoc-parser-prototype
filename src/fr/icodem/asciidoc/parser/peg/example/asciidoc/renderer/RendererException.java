package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer;

public class RendererException extends RuntimeException {
    public RendererException() {
    }

    public RendererException(String message) {
        super(message);
    }

    public RendererException(String message, Throwable cause) {
        super(message, cause);
    }

    public RendererException(Throwable cause) {
        super(cause);
    }

    public RendererException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
