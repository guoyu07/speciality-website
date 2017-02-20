package com.hiczp.web.speciality.controller;

import com.alibaba.fastjson.JSON;
import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.ConfigRepository;
import com.hiczp.web.speciality.repository.SortRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SortRepository sortRepository;
    private ArticleRepository articleRepository;
    private ConfigRepository configRepository;

    public HomeController(SortRepository sortRepository, ArticleRepository articleRepository, ConfigRepository configRepository) {
        this.sortRepository = sortRepository;
        this.articleRepository = articleRepository;
        this.configRepository = configRepository;
    }

    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("/home/index");
        SortEntity mainIndexSort = null;
        List<ArticleEntity> mainIndexSortArticles = new ArrayList<>();
        List<SortEntity> indexSorts = new ArrayList<>();
        List<List<ArticleEntity>> indexSortArticles = new ArrayList<>();
        List<String> carouselImages;

        //mainIndexSort
        try {
            Integer mainIndexSortId = Integer.valueOf(configRepository.findByKey("mainIndexSort").getValue());
            mainIndexSort = sortRepository.findOne(mainIndexSortId);
            mainIndexSortArticles = articleRepository.findTop3BySortOrderByCreateTimeDesc(mainIndexSort.getId());
            //将文章内容转成摘要, 摘取前一百个字
            mainIndexSortArticles.parallelStream().forEach(articleEntity -> {
                Document document = Jsoup.parse(articleEntity.getContent());
                articleEntity.setContent(document.text().substring(0, 100));
            });
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
        indexSorts.forEach(sortEntity -> indexSortArticles.add(articleRepository.findTop5BySortOrderByCreateTimeDesc(sortEntity.getId())));

        //carousel
        carouselImages = JSON.parseArray(configRepository.findByKey("carouselImages").getValue(), String.class);

        return modelAndView.addObject("mainIndexSort", mainIndexSort)
                .addObject("mainIndexSortArticles", mainIndexSortArticles)
                .addObject("indexSorts", indexSorts)
                .addObject("indexSortArticles", indexSortArticles)
                .addObject("carouselImages", carouselImages);
    }
}
