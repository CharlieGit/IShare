package com.dp.ishare.exception;

public class FileException extends RuntimeException{
    private String redirect;

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, String redirect) {
        super(message);
        this.redirect = redirect;
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
