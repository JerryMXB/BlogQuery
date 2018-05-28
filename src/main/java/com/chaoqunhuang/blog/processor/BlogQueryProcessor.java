package com.chaoqunhuang.blog.processor;

import com.chaoqunhuang.blog.converter.ResultDocumentSerializer;
import com.chaoqunhuang.blog.converter.SnippetGenerator;
import com.chaoqunhuang.blog.model.ResultDocument;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
public class BlogQueryProcessor {
    private IndexSearcher isearcher;
    private final String FIELD_SEARCH = "contents";
    private final int RESULT_NUM = 3;

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

    /**
     * Search the results given a query
     * @param queryText A string query
     * @return The json results which are serialized from the list of result documents
     * @throws IOException An IOException will be thrown if it fails to create such json message
     */
    public String search(String queryText) throws IOException {
        return ResultDocumentSerializer.serializeResultDocument(booleanQuery(queryText));
    }

    /**
     * Boolean Query search @see <a href="https://docs.oracle.com/cd/E26180_01/Search.94/ATGSearchQueryRef/html/s0202booleanqueries01.html">Boolean Query</a>
     * @param queryText A string query
     * @return The list of result documents
     * @throws IOException
     */
    private List<ResultDocument> booleanQuery(String queryText) throws IOException {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        for (String term : queryText.toLowerCase().split("\\s")) {
            builder.add(new TermQuery(new Term(FIELD_SEARCH, term)), BooleanClause.Occur.MUST);
        }

        Query query = builder.build();

        ScoreDoc[] hits = isearcher.search(query, RESULT_NUM).scoreDocs;
        // Iterate through the results:
        List<ResultDocument> docs = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            Document hitDoc = isearcher.doc(hit.doc);
            docs.add(new ResultDocument(hitDoc.getField("filename").stringValue(),
                    hit.score,
                    SnippetGenerator.generateSnippet(hitDoc, queryText)));
        }

        log.info("HitDocs:" + docs.toString());
        return docs;
    }


}
