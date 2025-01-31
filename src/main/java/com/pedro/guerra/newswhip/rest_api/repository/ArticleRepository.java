package com.pedro.guerra.newswhip.rest_api.repository;

import com.pedro.guerra.newswhip.rest_api.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    List<ArticleEntity> findByCountryCode(String countryCode);

}
