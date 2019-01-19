package com.example.webpageparser.interaction;

public enum MessageType {
    INVALID("invalid"), LOADING("loading"), READING("reading"), UNKNOWN("unknown");
    private final String type;

    public static final MessageType[] ERRORS = new MessageType[]{INVALID, LOADING, READING, UNKNOWN};

    MessageType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
