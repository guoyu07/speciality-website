package com.hiczp.web.speciality.controller.api.admin;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.SortRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by czp on 17-3-13.
 */
@RestController
@RequestMapping("/api/admin/sort")
public class AdminSortAPIController {
    private SortRepository sortRepository;
    private ArticleRepository articleRepository;

    public AdminSortAPIController(SortRepository sortRepository, ArticleRepository articleRepository) {
        this.sortRepository = sortRepository;
        this.articleRepository = articleRepository;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Integer id) {
        sortRepository.delete(id);

        //将该分类下的文章的分类都改为 0
        List<ArticleEntity> articleEntities = articleRepository.findBySort(id);
        articleEntities.forEach(articleEntity -> articleEntity.setSort(0));
        articleRepository.save(articleEntities);

        return new ResponseEntity(HttpStatus.OK);
    }
}
