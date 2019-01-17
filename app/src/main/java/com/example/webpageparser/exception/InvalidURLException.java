package com.example.webpageparser.exception;

import java.io.IOException;

public class InvalidURLException extends IOException {
    public InvalidURLException(String message, Throwable cause) {
        super(message, cause);
    }
}
