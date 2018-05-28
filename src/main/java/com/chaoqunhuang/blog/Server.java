package com.chaoqunhuang.blog;


import com.chaoqunhuang.blog.processor.BlogQueryProcessor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Log4j2
public class Server extends AbstractVerticle {

    @Override
    public void start() {
        log.info("Server Starts");
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Enter the path where the index is: (e.g. /tmp/index)");
        String indexPath;
        try {
            indexPath = br.readLine();
            log.info(String.format("Index Path is:%s", indexPath));
        } catch (IOException e) {
            System.out.println("Cannot open index file");
            throw new RuntimeException(e);
        }

        HttpServer server = vertx.createHttpServer();

        BlogQueryProcessor blogQueryProcessor = new BlogQueryProcessor(indexPath);

        server.requestHandler(request -> {
            // This handler gets called for each request that arrives on the server
            HttpServerResponse response = request.response();
            final String keyWord = request.getParam("keyword");

            if (keyWord != null) {
                log.info("Searching word:" + keyWord);
                String sb = null;

                try {
                    sb = blogQueryProcessor.search(keyWord);
                } catch (Exception e) {
                    log.info(e.getMessage() + e.getCause());
                }
                response.putHeader("content-type", "text/plain");
                response.putHeader("Access-Control-Allow-Origin", "*");


                // Write to the response and end it
                response.end(sb);
            }
        });
        server.listen(9090);
    }
}
