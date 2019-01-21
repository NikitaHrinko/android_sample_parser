package com.example.webpageparser.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TextParser {

    Set<String> extractEmails(List<String> source) {
        String emailPattern = "\\b([a-z0-9_.-]+)@([a-z0-9_.-]+[a-z])";
        return extractByPattern(source, emailPattern);
    }

    Set<String> extractLinks(List<String> source) {
        String linkPattern = "\\b((((https?|ftp|file)://)((www|ftp|docs)[.])?)|((www|ftp|docs)[.]))[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        return extractByPattern(source, linkPattern);
    }

    Set<String> extractByPattern(List<String> source, String regexPattern) {
        Set<String> matchList = new HashSet<>();
        Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);

        for (String line : source) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String matchingResult = matcher.group();
                matchList.add(matchingResult);
            }
        }

        return matchList;
    }
}
