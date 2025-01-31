package com.pedro.guerra.newswhip.rest_api.controller;

import com.pedro.guerra.newswhip.rest_api.entity.ArticleEntity;
import com.pedro.guerra.newswhip.rest_api.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/report/{countrycode}")
    public ResponseEntity<?> getReport(@PathVariable("countrycode") String countryCode) {
        return articleService.getArticle(countryCode);
    }

    @PostMapping("/article")
    public ResponseEntity<?> addArticle(@RequestBody ArticleEntity articleEntity) {
        return articleService.insertArticle(articleEntity);
    }

    @DeleteMapping("/article")
    public ResponseEntity<?> deleteArticle(@RequestParam String url) {
        return articleService.deleteArticle(url);
    }

}
