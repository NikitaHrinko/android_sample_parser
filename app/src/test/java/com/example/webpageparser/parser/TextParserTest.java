package com.example.webpageparser.parser;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TextParserTest {

    private static final List<String> sampleData = Arrays.asList("Text text example@example.com, text text http://www.example.com text",
            "email@example.com. first@email.com Text https://docs.example.com, https://ftp.ex.com text text",
            "Text text www.foofle.com, docs.foo.com text");

    @Test
    public void extractEmailsTest() {
        // GIVEN
        List<String> expectedEmails = Arrays.asList("example@example.com", "email@example.com", "first@email.com");
        TextParser parser = new TextParser();
        Set<String> results = new HashSet<>();
        // WHEN
        parser.extractEmails(sampleData, results);
        // THEN
        assertEquals("Array sizes don't match", expectedEmails.size(), results.size());
        for (int i = 0; i < expectedEmails.size(); i++) {
            String expected = expectedEmails.get(i);
            assertTrue("Failed to detect email #" + i + " : " + expected, results.contains(expected));
        }
    }

    @Test
    public void extractLinksTest() {
        // GIVEN
        List<String> expectedLinks = Arrays.asList("http://www.example.com", "https://docs.example.com", "https://ftp.ex.com", "www.foofle.com", "docs.foo.com");
        TextParser parser = new TextParser();
        // WHEN
        Set<String> results = parser.extractLinks(sampleData);
        // THEN
        assertEquals("Array sizes don't match", expectedLinks.size(), results.size());
        for (int i = 0; i < expectedLinks.size(); i++) {
            String expected = expectedLinks.get(i);
            assertTrue("Failed to detect link #" + i + " : " + expected, results.contains(expected));
        }
    }
}