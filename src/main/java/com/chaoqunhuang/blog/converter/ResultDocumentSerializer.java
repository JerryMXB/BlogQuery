package com.chaoqunhuang.blog.converter;


import com.chaoqunhuang.blog.model.ResultDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class ResultDocumentSerializer {
    /**
     * Serialize the list of result documents to json message
     * @param resultDocuments List of result documents to serialize
     * @return the serialzed json message
     * @throws IOException An IOException will be thrown if it fails to convert the json message
     */
    public static String serializeResultDocument(List<ResultDocument> resultDocuments) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String res = objectMapper.writeValueAsString(resultDocuments);
        log.info("Serialized Json Message:" + res);
        return res;
    }
}
