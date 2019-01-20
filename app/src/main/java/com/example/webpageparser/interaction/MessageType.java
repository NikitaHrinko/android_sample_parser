package com.example.webpageparser.interaction;

public class MessageType {
    public static final String INVALID = "invalid",
            LOADING = "loading",
            READING = "reading",
            UNKNOWN = "unknown",
            PROGRESS_PERCENTAGE = "progress_percentage",
            PROGRESS_STATUS = "progress_status",
            EMAILS = "emails";
    public static final String[] ERRORS = new String[]{INVALID, LOADING, READING, UNKNOWN};
}
