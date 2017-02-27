package com.hiczp.web.speciality.service;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.repository.ArticleRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

    public String getSummary(String content, int wordCountLimit) {
        Document document = Jsoup.parse(content);
        String text = document.text();
        if (text.length() > wordCountLimit) {
            text = text.substring(0, wordCountLimit) + "...";
        }
        return text;
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
