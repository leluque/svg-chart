package br.com.luque.svgchart.dataset.exception;

public class EmptyDataSetException extends Exception {

    public EmptyDataSetException() {
    }

    public EmptyDataSetException(String message) {
        super(message);
    }

    public EmptyDataSetException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyDataSetException(Throwable cause) {
        super(cause);
    }

    public EmptyDataSetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
