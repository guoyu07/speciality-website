package com.hiczp.web.speciality.controller.admin;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.exception.ArticleNotFoundException;
import com.hiczp.web.speciality.model.ArticleFormModel;
import com.hiczp.web.speciality.model.ArticleListFormModel;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.UserRepository;
import com.hiczp.web.speciality.service.SortService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;

/**
 * Created by czp on 17-3-17.
 */
@Controller
@RequestMapping("/admin")
public class AdminArticleController {
    private SortService sortService;
    private ArticleRepository articleRepository;
    private UserRepository userRepository;

    public AdminArticleController(SortService sortService, ArticleRepository articleRepository, UserRepository userRepository) {
        this.sortService = sortService;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/article")
    public ModelAndView article(ModelAndView modelAndView, ArticleListFormModel articleListFormModel, Pageable pageable) {
        if (articleListFormModel.getWord() == null) {
            articleListFormModel.setWord("");
        }
        modelAndView.setViewName("/admin/article_list");
        return modelAndView.addObject("userId", userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId())
                .addObject("articleListFormModel", articleListFormModel)
                .addObject("articleEntities", articleRepository.findByTitleContainsOrderByCreateTimeDesc(articleListFormModel.getWord(), pageable))
                .addObject("sortEntities", sortService.getTreeListText());
    }

    @GetMapping("/article/{id}")
    public ModelAndView article(ModelAndView modelAndView, @PathVariable Integer id) {
        ArticleEntity articleEntity = articleRepository.findOne(id);
        if (articleEntity == null) {
            throw new ArticleNotFoundException();
        }
        ArticleFormModel articleFormModel = new ArticleFormModel();
        articleFormModel.setId(articleEntity.getId());
        articleFormModel.setTitle(articleEntity.getTitle());
        articleFormModel.setTag(articleEntity.getTag());
        articleFormModel.setImage(articleEntity.getImage());
        articleFormModel.setContent(articleEntity.getContent());
        articleFormModel.setSort(articleEntity.getSort());
        articleFormModel.setCreateTime(articleEntity.getCreateTime());
        articleFormModel.setPublish(articleEntity.getPublish());
        modelAndView.setViewName("/admin/article");
        return modelAndView.addObject("articleFormModel", articleFormModel)
                .addObject("sortEntities", sortService.getTreeListText());
    }

    @GetMapping("/new_article")
    public ModelAndView newArticle(ModelAndView modelAndView, ArticleFormModel articleFormModel) {
        articleFormModel.setCreateTime(new Timestamp(System.currentTimeMillis()));
        modelAndView.setViewName("/admin/article");
        return modelAndView.addObject("articleFormModel", articleFormModel)
                .addObject("sortEntities", sortService.getTreeListText());
    }
}
