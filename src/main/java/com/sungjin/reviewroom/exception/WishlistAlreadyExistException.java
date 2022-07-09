package com.sungjin.reviewroom.exception;

public class WishlistAlreadyExistException extends RuntimeException {
    
    private static final long serialVersionUID = 5861310537366287163L;

    public WishlistAlreadyExistException() {
        super();
    }

    public WishlistAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WishlistAlreadyExistException(final String message) {
        super(message);
    }

    public WishlistAlreadyExistException(final Throwable cause) {
        super(cause);
    }
    
}
