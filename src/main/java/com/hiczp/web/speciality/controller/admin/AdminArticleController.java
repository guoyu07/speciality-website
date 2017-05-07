package com.hiczp.web.speciality.controller.admin;

import com.hiczp.web.speciality.model.ArticleFormModel;
import com.hiczp.web.speciality.service.SortService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.awt.print.Pageable;
import java.sql.Timestamp;

/**
 * Created by czp on 17-3-17.
 */
@Controller
@RequestMapping("/admin")
public class AdminArticleController {
    private SortService sortService;

    public AdminArticleController(SortService sortService) {
        this.sortService = sortService;
    }

    @GetMapping("/article")
    public ModelAndView article(ModelAndView modelAndView, Pageable pageable) {
        modelAndView.setViewName("/admin/article_list");
        return modelAndView;
    }

    @GetMapping("/article/{id}")
    public ModelAndView article(ModelAndView modelAndView, @PathVariable Integer id) {
        modelAndView.setViewName("/admin/article");
        return modelAndView;
    }

    @GetMapping("/new_article")
    public ModelAndView newArticle(ModelAndView modelAndView, ArticleFormModel articleFormModel) {
        articleFormModel.setCreateTime(new Timestamp(System.currentTimeMillis()));
        modelAndView.setViewName("/admin/article");
        return modelAndView.addObject("articleFormModel", articleFormModel)
                .addObject("sortEntities", sortService.getTreeListText());
    }
}
