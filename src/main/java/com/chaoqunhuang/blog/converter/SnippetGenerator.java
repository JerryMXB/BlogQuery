package com.chaoqunhuang.blog.converter;

import lombok.extern.log4j.Log4j2;
import org.apache.lucene.document.Document;

@Log4j2
public class SnippetGenerator {
    /**
     * Generate Snippets for the key word
     * @param doc Hit Document
     * @param keyWord Keyword
     * @return The generated Snippets
     */
    public static String generateSnippet(Document doc, String keyWord) {
        StringBuilder sb = new StringBuilder();
        String contents = doc.getField("contents").stringValue();
        String lowerCaseContents = contents.toLowerCase();
        String firstKeyWord = keyWord.split("\\s")[0];
        int firstKeyWordIndex = lowerCaseContents.indexOf(firstKeyWord.toLowerCase());
        sb.append(contents.substring(
                firstKeyWordIndex - 50 > 0 ? firstKeyWordIndex - 50 : 0,
                firstKeyWordIndex
        ));
        sb.append("<b>").append(firstKeyWord).append("</b>");
        sb.append(contents.substring(
                firstKeyWordIndex + firstKeyWord.length(),
                firstKeyWordIndex + 50 < contents.length() ? firstKeyWordIndex + 50 : contents.length() - 1
        ));
        return sb.toString();
    }
}
