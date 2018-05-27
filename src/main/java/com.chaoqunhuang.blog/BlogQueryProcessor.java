package com.chaoqunhuang.blog;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Paths;

public class BlogQueryProcessor {
    private IndexSearcher isearcher;
    private final String FIELD_SEARCH = "contents";

    public BlogQueryProcessor(String indexPath) {
        try {
            Directory directory = FSDirectory.open(Paths.get(indexPath));
            DirectoryReader ireader = DirectoryReader.open(directory);
            isearcher = new IndexSearcher(ireader);
        } catch (IOException e) {
            System.out.println("Cannot open index path");
            throw new RuntimeException(e);
        }
    }

    public List<Document> search(String queryText) throws IOException {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        for (String term : queryText.toLowerCase().split("\\s")) {
            builder.add(new TermQuery(new Term(FIELD_SEARCH, term)), BooleanClause.Occur.MUST);
        }

        Query query = builder.build();

        ScoreDoc[] hits = isearcher.search(query, 5).scoreDocs;
        // Iterate through the results:
        List<Document> docs = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            Document hitDoc = isearcher.doc(hit.doc);
            docs.add(hitDoc);
        }
        return docs;
    }


}
