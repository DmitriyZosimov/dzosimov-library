package com.epam.brest.service.exception;

public class BookCreationException extends Exception{
    public BookCreationException() {
    }

    public BookCreationException(String message) {
        super(message);
    }

    public BookCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookCreationException(Throwable cause) {
        super(cause);
    }

    public BookCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
