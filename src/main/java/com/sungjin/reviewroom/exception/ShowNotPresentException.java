package com.sungjin.reviewroom.exception;

public class ShowNotPresentException extends RuntimeException {
    
    private static final long serialVersionUID = 5861310537366287163L;

    public ShowNotPresentException() {
        super();
    }

    public ShowNotPresentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ShowNotPresentException(final String message) {
        super(message);
    }

    public ShowNotPresentException(final Throwable cause) {
        super(cause);
    }
    
}
