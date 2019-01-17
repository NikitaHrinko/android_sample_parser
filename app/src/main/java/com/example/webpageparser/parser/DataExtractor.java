package com.example.webpageparser.parser;

import com.example.webpageparser.reader.SourceTextReader;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataExtractor {

    private List<String> getEmails(Set<String> visitedLinks, String source, int depthLevel) throws IOException {
        if (visitedLinks.contains(source)) {
            return Collections.emptyList();
        }
        visitedLinks.add(source);

        SourceTextReader reader = new SourceTextReader();
        TextParser parser = new TextParser();

        List<String> currentPageText = reader.getTextAsList(source);

        List<String> emails = parser.extractEmails(currentPageText);

        if (depthLevel < 2) {
            return emails;
        }

        List<String> linksOnCurrentPage = parser.extractLinks(currentPageText);
        Stream<String> emailsStream = emails.stream();

        for (String link : linksOnCurrentPage) {
            List<String> subPageEmails = getEmails(visitedLinks, link, depthLevel - 1);
            emailsStream = Stream.concat(emailsStream, subPageEmails.stream());
        }

        return emailsStream.collect(Collectors.toList());
    }

    public List<String> getEmails(String source, int depthLevel) throws IOException {
        return getEmails(new HashSet<>(), source, depthLevel);
    }
}
