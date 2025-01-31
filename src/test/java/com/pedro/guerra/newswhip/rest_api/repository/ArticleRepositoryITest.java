package com.pedro.guerra.newswhip.rest_api.repository;

import com.pedro.guerra.newswhip.rest_api.entity.ArticleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.pedro.guerra.newswhip.rest_api.constants.TestConstants.COUNTRYCODE_IE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ArticleRepositoryITest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void testFindByCountryCode() {
        List<ArticleEntity> articleEntities = articleRepository.findByCountryCode(COUNTRYCODE_IE);

        assertEquals(3, articleEntities.size());
        assertEquals("http://www.rte.ie/news/politics/2018/1004/1001034-cso/", articleEntities.get(0).getUrl());
        assertEquals(20, articleEntities.get(0).getSocialScore());
        assertEquals(COUNTRYCODE_IE, articleEntities.get(0).getCountryCode());
        assertEquals("https://www.rte.ie/news/weather/2025/0110/1007-weather-cold-snap/", articleEntities.get(1).getUrl());
        assertEquals(60, articleEntities.get(1).getSocialScore());
        assertEquals(COUNTRYCODE_IE, articleEntities.get(1).getCountryCode());
        assertEquals("https://www.irishtimes.com/opinion/cartoon/2025/martyn-turner/", articleEntities.get(2).getUrl());
        assertEquals(50, articleEntities.get(2).getSocialScore());
        assertEquals(COUNTRYCODE_IE, articleEntities.get(2).getCountryCode());
    }

}
