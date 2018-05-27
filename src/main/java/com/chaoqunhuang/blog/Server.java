package com.chaoqunhuang.blog;

import java.util.List;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import org.apache.lucene.document.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Server extends AbstractVerticle {
    @Override
    public void start() {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Enter the path where the index is: (e.g. /tmp/index)");

        String indexPath;

        HttpServer server = vertx.createHttpServer();

        try {
            indexPath = br.readLine();
        } catch (IOException e) {
            System.out.println("Cannot open index file");
            throw new RuntimeException(e);
        }

        BlogQueryProcessor blogQueryProcessor = new BlogQueryProcessor(indexPath);

        server.requestHandler(request -> {
            // This handler gets called for each request that arrives on the server
            HttpServerResponse response = request.response();
            final String keyWord = request.getParam("keyword");

            if (keyWord != null) {
                System.out.println("Searching word:" + keyWord);
                StringBuilder sb = new StringBuilder();

                try {
                    List<Document> hits = blogQueryProcessor.search(keyWord);
                    sb.append("{\"hits\":[");
                    hits.forEach(hit ->
                            sb.append("\"").append(hit.getField("filename").stringValue()).append("\","));
                    if (hits.size() != 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append("]}");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.putHeader("content-type", "text/plain");
                response.putHeader("Access-Control-Allow-Origin", "*");


                // Write to the response and end it
                response.end(sb.toString());
            }
        });
        server.listen(9090);
    }
}
