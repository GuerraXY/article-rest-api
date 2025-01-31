package com.pedro.guerra.newswhip.rest_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleResponse {

    private String domain;
    private int urls;
    private int agg_social_score;

}
