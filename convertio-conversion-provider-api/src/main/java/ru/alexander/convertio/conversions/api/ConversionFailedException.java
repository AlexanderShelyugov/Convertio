package ru.alexander.convertio.conversions.api;

public class ConversionFailedException extends Exception {
    public ConversionFailedException() {
        super();
    }

    public ConversionFailedException(String message) {
        super(message);
    }

    public ConversionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConversionFailedException(Throwable cause) {
        super(cause);
    }

    protected ConversionFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
