package com.example.webpageparser.parser;

import com.example.webpageparser.reader.SourceTextReader;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataExtractor {

    private void getEmails(Set<String> visitedLinks, String source, int depthLevel, Set<String> emails) throws IOException {
        if (visitedLinks.contains(source)) {
            return;
        }
        visitedLinks.add(source);

        SourceTextReader reader = new SourceTextReader();
        TextParser parser = new TextParser();

        List<String> currentPageText = reader.getTextAsList(source);

        parser.extractEmails(currentPageText, emails);

        if (depthLevel < 2) {
            return;
        }

        Set<String> linksOnCurrentPage = parser.extractLinks(currentPageText);

        for (String link : linksOnCurrentPage) {
            getEmails(visitedLinks, link, depthLevel - 1, emails);
        }
    }

    public Set<String> getEmails(String source, int depthLevel) throws IOException {
        Set<String> emails = new HashSet<>();
        getEmails(new HashSet<>(), source, depthLevel, emails);
        return emails;
    }
}
