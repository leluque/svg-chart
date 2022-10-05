package br.com.luque.svgchart.painter.exception;

public class SVGCouldNotBeLoadedException extends Exception {

    public SVGCouldNotBeLoadedException() {
    }

    public SVGCouldNotBeLoadedException(String message) {
        super(message);
    }

    public SVGCouldNotBeLoadedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SVGCouldNotBeLoadedException(Throwable cause) {
        super(cause);
    }

    public SVGCouldNotBeLoadedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
