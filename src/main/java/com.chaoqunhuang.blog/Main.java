package com.chaoqunhuang.blog;

import org.apache.lucene.document.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String args[]) throws IOException{
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Enter the path where the index is: (e.g. /tmp/index)");
        String indexPath = br.readLine();

        BlogQueryProcessor blogQueryProcessor = new BlogQueryProcessor(indexPath);

        System.out.println("Enter the query:");
        String query = br.readLine();
        List<Document> documents = blogQueryProcessor.search(query);

        System.out.println(documents.get(0).getField("filename").stringValue());
    }
}
