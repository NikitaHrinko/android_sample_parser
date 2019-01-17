package com.example.webpageparser.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TextParser {

    void extractEmails(List<String> source, Set<String> emails) {
        String emailPattern = "\\b([a-z0-9_.-]+)@([a-z0-9_.-]+[a-z])";
        extractByPattern(source, emailPattern, emails);
    }

    Set<String> extractLinks(List<String> source) {
        String linkPattern = "\\b((((https?|ftp|file)://)?(www|ftp|docs)[.])|((www|ftp|docs)[.]))[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        return extractByPattern(source, linkPattern, new HashSet<>());
    }

    Set<String> extractByPattern(List<String> source, String regexPattern, Set<String> matchList) {

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
