package com.hiczp.web.speciality.service;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.repository.ArticleRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by czp on 17-2-23.
 */
@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Async
    public void viewArticleAsync(Integer id) {
        ArticleEntity articleEntity = articleRepository.findOne(id);
        articleEntity.setViews(articleEntity.getViews() + 1);
        articleRepository.save(articleEntity);
    }

    @Async
    public void viewArticleAsync(ArticleEntity articleEntity) {
        viewArticleAsync(articleEntity.getId());
    }
}
