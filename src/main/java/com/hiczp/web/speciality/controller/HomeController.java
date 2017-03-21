package com.hiczp.web.speciality.controller;

import com.alibaba.fastjson.JSON;
import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.ConfigRepository;
import com.hiczp.web.speciality.repository.SortRepository;
import com.hiczp.web.speciality.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czp on 17-2-8.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private ArticleService articleService;
    private SortRepository sortRepository;
    private ArticleRepository articleRepository;
    private ConfigRepository configRepository;

    public HomeController(ArticleService articleService, SortRepository sortRepository, ArticleRepository articleRepository, ConfigRepository configRepository) {
        this.articleService = articleService;
        this.sortRepository = sortRepository;
        this.articleRepository = articleRepository;
        this.configRepository = configRepository;
    }

    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView) {
        SortEntity mainIndexSort = null;
        List<ArticleEntity> mainIndexSortArticles = new ArrayList<>();
        List<SortEntity> indexSorts = new ArrayList<>();
        List<List<ArticleEntity>> indexSortArticles = new ArrayList<>();
        List<String> carouselImages;

        //mainIndexSort
        try {
            Integer mainIndexSortId = Integer.valueOf(configRepository.findByKey("mainIndexSort").getValue());
            mainIndexSort = sortRepository.findOne(mainIndexSortId);
            mainIndexSortArticles = articleRepository.findTop3BySortAndPublishTrueOrderByCreateTimeDesc(mainIndexSort.getId());
            //将文章内容转成摘要, 摘取前 150 个字
            mainIndexSortArticles.parallelStream().forEach(articleEntity -> articleEntity.setContent(articleService.getSummary(articleEntity.getContent(), 150)));
        } catch (NumberFormatException e) {
            logger.error("未配置 mainIndexSort");
        } catch (NullPointerException e) {
            logger.error("当前设置的 mainIndexSort 指向不存在分类");
        }

        //indexSorts
        List<Integer> indexSortIds = JSON.parseArray(configRepository.findByKey("indexSorts").getValue(), Integer.class);
        indexSortIds.forEach(id -> {
            SortEntity sortEntity = sortRepository.findOne(id);
            if (sortEntity != null) {
                indexSorts.add(sortEntity);
            } else {
                logger.error(String.format("当前设置的 indexSorts 中的分类 %d 指向不存在对象", id));
            }
        });
        indexSorts.forEach(sortEntity -> indexSortArticles.add(articleRepository.findTop5BySortAndPublishTrueOrderByCreateTimeDesc(sortEntity.getId())));

        //carousel
        carouselImages = JSON.parseArray(configRepository.findByKey("carouselImages").getValue(), String.class);

        modelAndView.setViewName("/home/index");
        return modelAndView.addObject("mainIndexSort", mainIndexSort)
                .addObject("mainIndexSortArticles", mainIndexSortArticles)
                .addObject("indexSorts", indexSorts)
                .addObject("indexSortArticles", indexSortArticles)
                .addObject("carouselImages", carouselImages);
    }

    @GetMapping("/search")
    public ModelAndView search(ModelAndView modelAndView, String word, Pageable pageable) {
        Page<ArticleEntity> results = articleRepository.findByTitleContainsOrContentContainsOrTagContainsAndPublishTrueOrderByCreateTimeDesc(word, word, word, pageable);
        results.getContent().parallelStream().forEach(articleEntity -> articleEntity.setContent(articleService.getSummary(articleEntity.getContent(), 200)));

        modelAndView.setViewName("/home/search");
        return modelAndView.addObject("word", word)
                .addObject("results", results)
                .addObject("path", "/search");
    }
}
