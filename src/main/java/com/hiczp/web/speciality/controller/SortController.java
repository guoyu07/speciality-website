package com.hiczp.web.speciality.controller;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.enumeration.SortType;
import com.hiczp.web.speciality.exception.SortNotFoundException;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.SortRepository;
import com.hiczp.web.speciality.service.ArticleService;
import com.hiczp.web.speciality.service.SortService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czp on 17-2-21.
 */
@Controller
@RequestMapping("/sort")
public class SortController {
    private SortService sortService;
    private ArticleService articleService;
    private SortRepository sortRepository;
    private ArticleRepository articleRepository;

    public SortController(SortService sortService, ArticleService articleService, SortRepository sortRepository, ArticleRepository articleRepository) {
        this.sortService = sortService;
        this.articleService = articleService;
        this.sortRepository = sortRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/{id}")
    public ModelAndView index(ModelAndView modelAndView, @PathVariable Integer id, Pageable pageable) {
        SortEntity sortEntity = sortRepository.findOne(id);
        if (sortEntity == null) {
            throw new SortNotFoundException();
        }
        Boolean isArticle = false;
        Page<ArticleEntity> articleEntities = null;
        ArticleEntity articleEntity = null;

        if (sortEntity.getType() == SortType.NORMAL) {   //普通分类
            articleEntities = articleRepository.findBySortAndPublishTrueOrderByCreateTimeDesc(id, pageable);
            //为根分类且无文章则跳转到下属的第一个子分类
            if (sortEntity.getParent() == 0 && articleEntities.getTotalElements() == 0) {
                //如果有子分类
                List<SortEntity> childSorts = sortService.getChildSorts(sortEntity);
                if (childSorts.size() != 0) {
                    modelAndView.setView(new RedirectView(String.valueOf(childSorts.get(0).getId())));
                    return modelAndView;
                }
            }
        } else if (sortEntity.getType() == SortType.ARTICLE) {   //文章分类
            articleEntity = articleRepository.findFirstBySortAndPublishTrueOrderByCreateTimeDesc(id);
            //对应的文章存在时
            if (articleEntity != null) {
                isArticle = true;
                articleService.viewArticleAsync(articleEntity);
            } else {    //对应的文章不存在时, 将返回值赋值为空的 Page
                articleEntities = new PageImpl<>(new ArrayList<>());
            }
        } else {    //分类类型非法
            throw new RuntimeException(String.format("Illegal sort type '%s'", sortEntity.getType()));
        }

        modelAndView.setViewName("/sort/index");
        return modelAndView.addObject("isArticle", isArticle)
                .addObject("articleEntities", articleEntities)
                .addObject("path", String.format("/sort/%d", id))
                .addObject("articleEntity", articleEntity)
                .addObject("sidebarSorts", sortService.getSidebarSorts(sortEntity))
                .addObject("sidebarActive", sortEntity)
                .addObject("breadcrumbsChain", sortService.getParentsChain(sortEntity));
    }
}
