package com.example.webpageparser.parser;

import android.os.Handler;

import com.example.webpageparser.exception.InvalidURLException;
import com.example.webpageparser.exception.PageLoadingException;
import com.example.webpageparser.exception.PageReadingException;
import com.example.webpageparser.interaction.MessageDispatcher;
import com.example.webpageparser.interaction.MessageType;
import com.example.webpageparser.reader.SourceTextReader;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataExtractor {

    private Handler messageHandler;

    public DataExtractor(Handler messageHandler) {
        this.messageHandler = Objects.requireNonNull(messageHandler);
    }

    private List<String> getFullText(Set<String> visitedLinks, String source, int depthLevel) {
        if (visitedLinks.contains(source)) {
            return Collections.emptyList();
        }
        visitedLinks.add(source);

        SourceTextReader reader = new SourceTextReader();
        TextParser parser = new TextParser();

        List<String> currentPageText;
        try {
            currentPageText = reader.getTextAsList(source);
        } catch (IOException e) {
            e.printStackTrace();
            dispatchMessages(e, source);
            return Collections.emptyList();
        }

        if (depthLevel < 2) {
            return currentPageText;
        }

        Set<String> linksOnCurrentPage = parser.extractLinks(currentPageText);
        Stream<String> textStream = currentPageText.stream();

        for (String link : linksOnCurrentPage) {
            List<String> subPageText = getFullText(visitedLinks, link, depthLevel - 1);
            textStream = Stream.concat(textStream, subPageText.stream());
        }

        return textStream.collect(Collectors.toList());
    }

    public Set<String> getEmails(String source, int depthLevel) {
        TextParser parser = new TextParser();
        List<String> fullText = getFullText(new HashSet<>(), source, depthLevel);
        return parser.extractEmails(fullText);
    }

    private void dispatchMessages(IOException exception, String source) {
        String messageType;

        if (exception instanceof InvalidURLException) {
            messageType = MessageType.INVALID.toString();
        } else if (exception instanceof PageLoadingException) {
            messageType = MessageType.LOADING.toString();
        } else if (exception instanceof PageReadingException) {
            messageType = MessageType.READING.toString();
        } else {
            messageType = MessageType.UNKNOWN.toString();
        }

        MessageDispatcher.dispatch(messageHandler, messageType, source);
    }
}
