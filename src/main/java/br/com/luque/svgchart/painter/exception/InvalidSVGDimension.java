package br.com.luque.svgchart.painter.exception;

public class InvalidSVGDimension extends Exception {

    public InvalidSVGDimension() {
    }

    public InvalidSVGDimension(String message) {
        super(message);
    }

    public InvalidSVGDimension(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSVGDimension(Throwable cause) {
        super(cause);
    }

    public InvalidSVGDimension(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
