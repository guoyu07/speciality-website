package com.hiczp.web.speciality.controller;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.exception.ArticleNotFoundException;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.SortRepository;
import com.hiczp.web.speciality.service.ArticleService;
import com.hiczp.web.speciality.service.SortService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by czp on 17-2-24.
 */
@Controller
@RequestMapping("/article")
public class ArticleController {
    private SortService sortService;
    private ArticleService articleService;
    private SortRepository sortRepository;
    private ArticleRepository articleRepository;

    public ArticleController(SortService sortService, ArticleService articleService, SortRepository sortRepository, ArticleRepository articleRepository) {
        this.sortService = sortService;
        this.articleService = articleService;
        this.sortRepository = sortRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/{id}")
    public ModelAndView index(ModelAndView modelAndView, @PathVariable Integer id) {
        ArticleEntity articleEntity = articleRepository.findOne(id);
        if (articleEntity == null) {
            throw new ArticleNotFoundException();
        }
        SortEntity sortEntity = sortRepository.findOne(articleEntity.getSort());
        articleService.viewArticleAsync(articleEntity);

        modelAndView.setViewName("/article/index");
        return modelAndView.addObject("articleEntity", articleEntity)
                .addObject("sidebarSorts", sortService.getSidebarSorts(sortEntity))
                .addObject("sidebarActive", sortEntity)
                .addObject("breadcrumbsChain", sortService.getParentsChain(sortEntity));
    }
}
