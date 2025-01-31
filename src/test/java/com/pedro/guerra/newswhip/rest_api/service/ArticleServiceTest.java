package com.pedro.guerra.newswhip.rest_api.service;

import com.pedro.guerra.newswhip.rest_api.entity.ArticleEntity;
import com.pedro.guerra.newswhip.rest_api.model.ArticleResponse;
import com.pedro.guerra.newswhip.rest_api.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pedro.guerra.newswhip.rest_api.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;
    @Mock
    private ArticleRepository articleRepository;

    @Test
    void testGetArticle_validCountryCode() {
        List<ArticleEntity> mockedArticleEntities = new ArrayList<>();
        mockedArticleEntities.add(mockArticleEntity("https://" + URL_IRISHTIMES + "/1", 20, COUNTRYCODE_IE));
        mockedArticleEntities.add(mockArticleEntity("https://" + URL_IRISHTIMES + "/2", 30, COUNTRYCODE_IE));
        mockedArticleEntities.add(mockArticleEntity("https://" + URL_RTE + "/1", 40, COUNTRYCODE_ES));

        when(articleRepository.findByCountryCode(any())).thenReturn(mockedArticleEntities);

        ResponseEntity<?> responseEntity = articleService.getArticle(COUNTRYCODE_IE);

        List<ArticleResponse> articleResponses = (ArrayList<ArticleResponse>) responseEntity.getBody();

        assertEquals(URL_IRISHTIMES, articleResponses.get(0).getDomain());
        assertEquals(2, articleResponses.get(0).getUrls());
        assertEquals(50, articleResponses.get(0).getAgg_social_score());
    }

    @Test
    void testGetArticle_invalidCountryCode() {
        ResponseEntity<?> responseEntity = articleService.getArticle(COUNTRYCODE_INVALID);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(BAD_REQUEST_MESSAGE_COUNTRYCODE, responseEntity.getBody());
    }

    @Test
    void testInsertArticle_validCountryCodeAndUrl() {
        String url = "http://" + URL_IRISHTIMES + "/1";
        ArticleEntity mockedArticleEntity = mockArticleEntity(url, 20, COUNTRYCODE_IE);

        when(articleRepository.findById(any())).thenReturn(Optional.empty());
        when(articleRepository.save(any())).thenReturn(mockedArticleEntity);

        ResponseEntity<?> responseEntity = articleService.insertArticle(mockedArticleEntity);

        ArticleEntity insertedArticle = (ArticleEntity) responseEntity.getBody();

        assertEquals(url, insertedArticle.getUrl());
        assertEquals(COUNTRYCODE_IE, insertedArticle.getCountryCode());
        assertEquals(20, insertedArticle.getSocialScore());
    }

    @Test
    void testInsertArticle_invalidCountryCode() {
        String url = "http://" + URL_IRISHTIMES + "/1";
        ArticleEntity mockedArticleEntity = mockArticleEntity(url, 20, COUNTRYCODE_INVALID);

        ResponseEntity<?> responseEntity = articleService.insertArticle(mockedArticleEntity);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(BAD_REQUEST_MESSAGE_COUNTRYCODE, responseEntity.getBody());
    }

    @Test
    void testInsertArticle_invalidUrl() {
        String invalidUrl = "http://" + URL_IRISHTIMES + "/h?s=^IXIC";
        ArticleEntity mockedArticleEntity = mockArticleEntity(invalidUrl, 20, COUNTRYCODE_IE);

        ResponseEntity<?> responseEntity = articleService.insertArticle(mockedArticleEntity);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(BAD_REQUEST_MESSAGE_INVALID_URL, responseEntity.getBody());
    }

    @Test
    void testInsertArticle_existingUrl() {
        ArticleEntity mockedArticleEntity = mockArticleEntity(URL_EXISTING, 20, COUNTRYCODE_IE);

        when(articleRepository.findById(any())).thenReturn(Optional.of(mockedArticleEntity));

        ResponseEntity<?> responseEntity = articleService.insertArticle(mockedArticleEntity);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals(BAD_REQUEST_MESSAGE_URL_ALREADY_EXISTENT, responseEntity.getBody());
    }

    @Test
    void testDeleteArticle_valid() {
        ArticleEntity mockedArticleEntity = mockArticleEntity(URL_EXISTING, 20, COUNTRYCODE_IE);

        when(articleRepository.findById(any())).thenReturn(Optional.of(mockedArticleEntity));
        doNothing().when(articleRepository).deleteById(any());

        ResponseEntity<?> responseEntity = articleService.deleteArticle(URL_EXISTING);

        Optional<ArticleEntity> optionalInsertedArticle = (Optional<ArticleEntity>) responseEntity.getBody();
        ArticleEntity insertedArticle = optionalInsertedArticle.get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(URL_EXISTING, insertedArticle.getUrl());
        assertEquals(COUNTRYCODE_IE, insertedArticle.getCountryCode());
        assertEquals(20, insertedArticle.getSocialScore());
    }

    @Test
    void testDeleteArticle_nonExistingUrl() {
        ArticleEntity mockedArticleEntity = mockArticleEntity(URL_NEW, 20, COUNTRYCODE_IE);

        when(articleRepository.findById(any())).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = articleService.deleteArticle(URL_NEW);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(BAD_REQUEST_MESSAGE_URL_NONEXISTENT, responseEntity.getBody());
    }

    private ArticleEntity mockArticleEntity(String url, int socialScore, String countryCode) {
        ArticleEntity articleEntity = new ArticleEntity();

        articleEntity.setUrl(url);
        articleEntity.setSocialScore(socialScore);
        articleEntity.setCountryCode(countryCode);

        return articleEntity;
    }

}
