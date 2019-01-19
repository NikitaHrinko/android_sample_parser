package com.example.webpageparser.reader;

import com.example.webpageparser.exception.InvalidURLException;
import com.example.webpageparser.exception.PageLoadingException;
import com.example.webpageparser.exception.PageReadingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SourceTextReader {
    public List<String> getTextAsList(String source) throws IOException {
        List<String> strings = new ArrayList<>();

        URL sourceUrl = null;
        try {
            sourceUrl = new URL(source);
        } catch (MalformedURLException e) {
            throw new InvalidURLException(source, e);
//            e.printStackTrace();
//            return Collections.emptyList();
        }

        BufferedReader in;
        try {
            in = new BufferedReader(
                    new InputStreamReader(sourceUrl.openStream()));
        } catch (IOException e) {
            throw new PageLoadingException(source, e);
//            e.printStackTrace();
//            return Collections.emptyList();
        }

        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                strings.add(inputLine);
            }
        } catch (IOException e) {
            throw new PageReadingException(source, e);
//            e.printStackTrace();
//            return Collections.emptyList();
        } finally {
            in.close();
        }

        return strings;
    }
}
