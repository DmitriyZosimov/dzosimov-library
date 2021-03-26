package com.epam.brest.service.exception;

public class ReaderCreationException extends Exception{
    public ReaderCreationException() {
        super();
    }

    public ReaderCreationException(String message) {
        super(message);
    }

    public ReaderCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReaderCreationException(Throwable cause) {
        super(cause);
    }
}
