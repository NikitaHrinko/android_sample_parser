package com.example.webpageparser.exception;

import java.io.IOException;

public class PageLoadingException extends IOException {
    public PageLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
