package com.chaoqunhuang.blog;

import com.chaoqunhuang.blog.processor.BlogQueryProcessor;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Log4j2
public class BenchTest {
    public static void main(String args[]) throws IOException{
        log.info("Server Starts");
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
//        System.out.println("Enter the path where the index is: (e.g. /tmp/index)");
//        String indexPath = br.readLine();
        String indexPath = "/Users/chaoqunhuang/projects/blogindex";

        BlogQueryProcessor blogQueryProcessor = new BlogQueryProcessor(indexPath);

        System.out.println("Enter the query:");
        String query = br.readLine();
        String res = blogQueryProcessor.search(query);
        System.out.println(res);
    }
}
