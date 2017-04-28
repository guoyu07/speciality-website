package com.hiczp.web.speciality.controller.admin;

import com.hiczp.web.speciality.configuration.AspectConfiguration;
import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.exception.ArticleNotFoundException;
import com.hiczp.web.speciality.model.ArticleFormModel;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.SortRepository;
import com.hiczp.web.speciality.service.SortService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.sql.Timestamp;

/**
 * Created by czp on 17-3-17.
 */
@Controller
@RequestMapping("/admin")
public class AdminArticleController {
    private SortService sortService;
    private ArticleRepository articleRepository;
    private SortRepository sortRepository;

    public AdminArticleController(SortService sortService, ArticleRepository articleRepository, SortRepository sortRepository) {
        this.sortService = sortService;
        this.articleRepository = articleRepository;
        this.sortRepository = sortRepository;
    }

    @GetMapping("/article")
    public ModelAndView article(ModelAndView modelAndView, Pageable pageable) {
        modelAndView.setViewName("/admin/article_list");
        return modelAndView.addObject("activeSidebarItem", "article");
    }

    @GetMapping("/article/{id}")
    public ModelAndView article(ModelAndView modelAndView, @PathVariable Integer id) {
        modelAndView.setViewName("/admin/article");
        return modelAndView.addObject("activeSidebarItem", "article");
    }

    @PostMapping("/article/{id}")
    public ModelAndView article(ModelAndView modelAndView, @PathVariable Integer id, @Valid ArticleFormModel articleFormModel, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            ArticleEntity articleEntity = articleRepository.findOne(id);
            if (articleEntity != null) {
                articleEntity.setTitle(articleFormModel.getTitle());
                articleEntity.setImage(articleFormModel.getImage());
                articleEntity.setContent(articleFormModel.getContent());
                articleEntity.setSort(articleFormModel.getSort());
                articleEntity.setPublish(articleFormModel.getPublish());
                articleRepository.save(articleEntity);
            } else {
                throw new ArticleNotFoundException();
            }
        }
        modelAndView.setViewName("/admin/article");
        return modelAndView.addObject("activeSidebarItem", "article");
    }

    @GetMapping("/new_article")
    public ModelAndView newArticle(ModelAndView modelAndView, ArticleFormModel articleFormModel) {
        articleFormModel.setCreateTime(new Timestamp(System.currentTimeMillis()));
        modelAndView.setViewName("/admin/article");
        return modelAndView.addObject("activeSidebarItem", "article")
                .addObject("articleFormModel", articleFormModel)
                .addObject("sortEntities", sortService.getTreeListText());
    }

    @GetMapping("/preview_article")
    public ModelAndView previewArticle(AspectConfiguration aspectConfiguration, ModelAndView modelAndView, ArticleFormModel articleFormModel) {
        Integer sort = articleFormModel.getSort();
        SortEntity sortEntity = sort != null ? sortRepository.findOne(sort) : null;
        modelAndView.setViewName("/admin/preview_article");
        modelAndView.addObject("articleFormModel", articleFormModel)
                .addObject("sidebarSorts", sortEntity != null ? sortService.getSidebarSorts(sortEntity) : sortService.getRootSorts())
                .addObject("sidebarActive", sortEntity)
                .addObject("breadcrumbsChain", sortEntity != null ? sortService.getParentsChain(sortEntity) : null);
        aspectConfiguration.afterNormalControllerReturning(modelAndView);
        return modelAndView;
    }
}
