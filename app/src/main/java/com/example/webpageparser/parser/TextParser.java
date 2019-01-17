package com.example.webpageparser.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TextParser {

    List<String> extractEmails(List<String> source) {
        String emailPattern = "\\b([a-z0-9_.-]+)@([a-z0-9_.-]+[a-z])";
        return extractByPattern(source, emailPattern);
    }

    List<String> extractLinks(List<String> source) {
        String linkPattern = "\\b((((https?|ftp|file)://)?(www|ftp|docs)[.])|((www|ftp|docs)[.]))[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        return extractByPattern(source, linkPattern);
    }

    List<String> extractByPattern(List<String> source, String regexPattern) {
        List<String> matchList = new LinkedList<>();

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
