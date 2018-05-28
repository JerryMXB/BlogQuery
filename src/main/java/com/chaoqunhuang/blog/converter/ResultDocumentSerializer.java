package com.chaoqunhuang.blog.converter;


import com.chaoqunhuang.blog.model.ResultDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class ResultDocumentSerializer {
    public static String serializeResultDocument(List<ResultDocument> resultDocument) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String res = objectMapper.writeValueAsString(resultDocument);
        log.info("Serialized Json Message:" + res);
        return res;
    }
}
