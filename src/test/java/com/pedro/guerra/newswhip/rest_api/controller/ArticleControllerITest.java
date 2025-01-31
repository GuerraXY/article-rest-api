package com.pedro.guerra.newswhip.rest_api.controller;

import com.pedro.guerra.newswhip.rest_api.entity.ArticleEntity;
import com.pedro.guerra.newswhip.rest_api.model.ArticleResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.pedro.guerra.newswhip.rest_api.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ArticleControllerITest {

    @LocalServerPort
    private int port;
    private static TestRestTemplate restTemplate;

    @BeforeAll
    static void setUpRestTemplate() {
        restTemplate = new TestRestTemplate();
    }

    @Test
    void testGetReport_validCountryCode() {
        String url = createUrlWithPort(REPORT_URL + "/" + COUNTRYCODE_IE);
        HttpEntity<String> request = new HttpEntity<>("");

        ResponseEntity<List<ArticleResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {});
        HttpStatus responseStatus = (HttpStatus) response.getStatusCode();

        assertEquals(HttpStatus.OK, responseStatus);
        assertEquals(2, response.getBody().size());
        assertEquals(URL_IRISHTIMES, response.getBody().get(0).getDomain());
        assertEquals(1, response.getBody().get(0).getUrls());
        assertEquals(50, response.getBody().get(0).getAgg_social_score());
        assertEquals(URL_RTE, response.getBody().get(1).getDomain());
        assertEquals(2, response.getBody().get(1).getUrls());
        assertEquals(80, response.getBody().get(1).getAgg_social_score());
    }

    @Test
    void testGetReport_invalidCountryCode() {
        String url = createUrlWithPort(REPORT_URL + "/" + COUNTRYCODE_INVALID);
        HttpEntity<String> request = new HttpEntity<>("");

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class);
        HttpStatus responseStatus = (HttpStatus) response.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
        assertEquals(BAD_REQUEST_MESSAGE_COUNTRYCODE, response.getBody());
    }

    @Test
    void testGetReport_urlWithBadSyntaxInDB() {
        String url = createUrlWithPort(REPORT_URL + "/" + COUNTRYCODE_PT);
        HttpEntity<String> request = new HttpEntity<>("");

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class);
        HttpStatus responseStatus = (HttpStatus) response.getStatusCode();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus);
        assertEquals(BAD_REQUEST_MESSAGE_URL_BAD_SYNTAX, response.getBody());
    }

    @Test
    void testAddArticle_validBody() {
        String url = createUrlWithPort(ARTICLE_URL);
        ArticleEntity mockedArticleEntity = mockArticleEntity(URL_NEW, COUNTRYCODE_IE);
        HttpEntity<ArticleEntity> request = new HttpEntity<>(mockedArticleEntity);

        ResponseEntity<ArticleEntity> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                ArticleEntity.class);
        HttpStatus responseStatus = (HttpStatus) response.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseStatus);
        assertEquals(COUNTRYCODE_IE, response.getBody().getCountryCode());
        assertEquals(URL_NEW, response.getBody().getUrl());
        assertEquals(100, response.getBody().getSocialScore());
    }

    @Test
    void testAddArticle_invalidCountryCode() {
        String url = createUrlWithPort(ARTICLE_URL);
        ArticleEntity mockedArticleEntity = mockArticleEntity(URL_NEW, COUNTRYCODE_INVALID);
        HttpEntity<ArticleEntity> request = new HttpEntity<>(mockedArticleEntity);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class);
        HttpStatus responseStatus = (HttpStatus) response.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
        assertEquals(BAD_REQUEST_MESSAGE_COUNTRYCODE, response.getBody());
    }

    @Test
    void testAddArticle_invalidUrl() {
        String url = createUrlWithPort(ARTICLE_URL);
        ArticleEntity mockedArticleEntity = mockArticleEntity(URL_INVALID, COUNTRYCODE_IE);
        HttpEntity<ArticleEntity> request = new HttpEntity<>(mockedArticleEntity);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class);
        HttpStatus responseStatus = (HttpStatus) response.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
        assertEquals(BAD_REQUEST_MESSAGE_INVALID_URL, response.getBody());
    }

    @Test
    void testAddArticle_existingUrl() {
        String url = createUrlWithPort(ARTICLE_URL);
        ArticleEntity mockedArticleEntity = mockArticleEntity(URL_EXISTING, COUNTRYCODE_IE);
        HttpEntity<ArticleEntity> request = new HttpEntity<>(mockedArticleEntity);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class);
        HttpStatus responseStatus = (HttpStatus) response.getStatusCode();

        assertEquals(HttpStatus.CONFLICT, responseStatus);
        assertEquals(BAD_REQUEST_MESSAGE_URL_ALREADY_EXISTENT, response.getBody());
    }

    @Test
    void testDeleteArticle_existingUrl() {
        String url = createUrlWithPort(ARTICLE_URL + "?url=" + URL_EXISTING);
        HttpEntity<String> request = new HttpEntity<>("");

        ResponseEntity<ArticleEntity> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                ArticleEntity.class);
        HttpStatus responseStatus = (HttpStatus) response.getStatusCode();

        assertEquals(HttpStatus.OK, responseStatus);
        assertEquals(COUNTRYCODE_IE, response.getBody().getCountryCode());
        assertEquals(URL_EXISTING, response.getBody().getUrl());
        assertEquals(20, response.getBody().getSocialScore());
    }

    @Test
    void testDeleteArticle_nonExistingUrl() {
        String url = createUrlWithPort(ARTICLE_URL + "?url=" + URL_NEW);
        HttpEntity<String> request = new HttpEntity<>("");

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                String.class);
        HttpStatus responseStatus = (HttpStatus) response.getStatusCode();

        assertEquals(HttpStatus.NOT_FOUND, responseStatus);
        assertEquals(BAD_REQUEST_MESSAGE_URL_NONEXISTENT, response.getBody());
    }

    private String createUrlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private ArticleEntity mockArticleEntity(String url, String countryCode) {
        ArticleEntity articleEntity = new ArticleEntity();

        articleEntity.setUrl(url);
        articleEntity.setSocialScore(100);
        articleEntity.setCountryCode(countryCode);

        return articleEntity;
    }



}
