package com.hiczp.web.speciality.controller.api;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.exception.ArticleNotFoundException;
import com.hiczp.web.speciality.repository.ArticleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by czp on 17-3-13.
 */
@RestController
@RequestMapping("/api/article")
public class ArticleAPIController {
    private ArticleRepository articleRepository;

    public ArticleAPIController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleEntity> getById(@PathVariable Integer id) {
        ArticleEntity articleEntity = articleRepository.findOne(id);
        if (articleEntity == null || !articleEntity.getPublish()) {
            throw new ArticleNotFoundException();
        } else {
            return new ResponseEntity<>(articleEntity, HttpStatus.OK);
        }
    }
}
