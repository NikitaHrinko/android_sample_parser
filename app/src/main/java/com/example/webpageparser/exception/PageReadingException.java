package com.example.webpageparser.exception;

import java.io.IOException;

public class PageReadingException extends IOException {
    public PageReadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
