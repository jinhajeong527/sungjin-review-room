package com.sungjin.reviewroom.exception;

public class ReviewerAlreadyExistException extends RuntimeException {
    
    private static final long serialVersionUID = 5861310537366287163L;

    public ReviewerAlreadyExistException() {
        super();
    }

    public ReviewerAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReviewerAlreadyExistException(final String message) {
        super(message);
    }

    public ReviewerAlreadyExistException(final Throwable cause) {
        super(cause);
    }
    
}
