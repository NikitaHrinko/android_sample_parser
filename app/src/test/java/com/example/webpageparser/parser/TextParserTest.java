package com.example.webpageparser.parser;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TextParserTest {

    private static final List<String> sampleData = Arrays.asList("Text text example@example.com, text text http://www.example.com text",
            "email@example.com. first@email.com Text https://docs.example.com, https://ftp.ex.com text text",
            "Text text www.foofle.com, docs.foo.com text");

    @Test
    public void extractEmailsTest() {
        // GIVEN
        List<String> expected = Arrays.asList("example@example.com", "email@example.com", "first@email.com");
        TextParser parser = new TextParser();
        // WHEN
        List<String> results = parser.extractEmails(sampleData);
        // THEN
        assertEquals("Array sizes don't match", expected.size(), results.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals("Failed to detect email #" + i, expected.get(i), results.get(i));
        }
    }

    @Test
    public void extractLinksTest() {
        // GIVEN
        List<String> expected = Arrays.asList("http://www.example.com", "https://docs.example.com", "https://ftp.ex.com", "www.foofle.com", "docs.foo.com");
        TextParser parser = new TextParser();
        // WHEN
        List<String> results = parser.extractLinks(sampleData);
        // THEN
        assertEquals("Array sizes don't match", expected.size(), results.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals("Failed to detect link #" + i, expected.get(i), results.get(i));
        }
    }
}