package com.pedro.guerra.newswhip.rest_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Articles")
public class ArticleEntity {

    @Id
    @Column(name = "url")
    private String url;

    @Column(name = "socialScore")
    private int socialScore;

    @Column(name = "countryCode")
    private String countryCode;

}
