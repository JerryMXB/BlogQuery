package com.chaoqunhuang.blog.model;

import lombok.Value;

@Value
public class ResultDocument {
    String url;
    float score;
    String snippet;
}
