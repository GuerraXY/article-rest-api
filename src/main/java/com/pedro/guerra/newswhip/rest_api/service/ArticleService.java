package com.pedro.guerra.newswhip.rest_api.service;

import com.pedro.guerra.newswhip.rest_api.entity.ArticleEntity;
import com.pedro.guerra.newswhip.rest_api.model.ArticleResponse;
import com.pedro.guerra.newswhip.rest_api.repository.ArticleRepository;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ResponseEntity<?> getArticle(String countryCode) {
        ResponseEntity<?> response;

        if (isValidCountryCode(countryCode)) {
            try {
                response = new ResponseEntity<>(aggregateSocialScores(countryCode), HttpStatus.OK);
            } catch (URISyntaxException e) {
                response = new ResponseEntity<>("One of the URLs under that domain doesn't have a correct Syntax", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            response = createBadGetRequestResponseEntity(countryCode);
        }

        return response;
    }

    public ResponseEntity<?> insertArticle(ArticleEntity articleEntity) {
        ResponseEntity<?> response;

        if (isValidCountryCode(articleEntity.getCountryCode()) && isValidUrl(articleEntity.getUrl())) {
            if (articleRepository.findById(articleEntity.getUrl()).isPresent()) {
                response = new ResponseEntity<>("There already exists an Article with that URL", HttpStatus.CONFLICT);
            } else {
                response = new ResponseEntity<>(articleRepository.save(articleEntity), HttpStatus.CREATED);
            }
        } else {
            response = createBadPostRequestResponseEntity(articleEntity.getCountryCode(), articleEntity.getUrl());
        }

        return response;
    }

    public ResponseEntity<?> deleteArticle(String url) {
        ResponseEntity<?> response;

        var articleToBeDeleted = articleRepository.findById(url);

        if (articleToBeDeleted.isPresent()) {
            articleRepository.deleteById(url);
            response = new ResponseEntity<>(articleToBeDeleted, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>("There is no Article with that URL", HttpStatus.NOT_FOUND);
        }

        return response;
    }

    private List<ArticleResponse> aggregateSocialScores(String countryCode) throws URISyntaxException {
        List<ArticleEntity> articlesWithCountryCode = articleRepository.findByCountryCode(countryCode);
        HashMap<String, Integer> domainsAndSocialScores = new HashMap<>();
        HashMap<String, Integer> domainsAndNumberOfUrls = new HashMap<>();
        URI uri;

        for (ArticleEntity articleEntity : articlesWithCountryCode) {
            uri = new URI(articleEntity.getUrl());
            if (!domainsAndSocialScores.containsKey(uri.getHost())) {
                domainsAndSocialScores.put(uri.getHost(), articleEntity.getSocialScore());
                domainsAndNumberOfUrls.put(uri.getHost(), 1);
            } else {
                domainsAndSocialScores.replace(uri.getHost(), domainsAndSocialScores.get(uri.getHost()) + articleEntity.getSocialScore());
                domainsAndNumberOfUrls.replace(uri.getHost(), domainsAndNumberOfUrls.get(uri.getHost()) + 1);
            }
        }

        return mapHashMapsToArticleResponse(domainsAndSocialScores, domainsAndNumberOfUrls);

    }

    private List<ArticleResponse> mapHashMapsToArticleResponse(HashMap<String, Integer> domainsAndSocialScores, HashMap<String, Integer> domainsAndNumberOfUrls) {
        List<ArticleResponse> articleResponses = new ArrayList<>();

        for (String domain : domainsAndSocialScores.keySet()) {
            articleResponses.add(new ArticleResponse(domain, domainsAndNumberOfUrls.get(domain), domainsAndSocialScores.get(domain)));
        }

        return articleResponses;
    }


    private boolean isValidCountryCode(String countryCode) {
        return countryCode != null && countryCode.length() == 2;
    }

    private boolean isValidUrl(String url) {
        return new UrlValidator().isValid(url);
    }

    private ResponseEntity<?> createBadGetRequestResponseEntity(String countryCode) {
        ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (countryCode.length() != 2) {
            response = new ResponseEntity<>("Country Code needs to have only 2 letters", HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    private ResponseEntity<?> createBadPostRequestResponseEntity(String countryCode, String url) {
        ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!isValidCountryCode(countryCode)) {
            response = new ResponseEntity<>("Country Code needs to have only 2 letters", HttpStatus.BAD_REQUEST);
        } else if (!isValidUrl(url)) {
            response = new ResponseEntity<>("Url is not valid", HttpStatus.BAD_REQUEST);
        }

        return response;
    }



}
