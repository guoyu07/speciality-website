package com.hiczp.web.speciality.controller.admin;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.exception.ArticleNotFoundException;
import com.hiczp.web.speciality.model.ArticleFormModel;
import com.hiczp.web.speciality.model.ArticleListFormModel;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.UserRepository;
import com.hiczp.web.speciality.service.SortService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.List;

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
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "createTime"));
        }
        Page<ArticleEntity> articleEntities = articleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            String word = articleListFormModel.getWord();
            if (word != null) {
                expressions.add(criteriaBuilder.like(root.get("title"), String.format("%%%s%%", word)));
            }
            Integer sortId = articleListFormModel.getSortId();
            if (sortId != null && sortId != -1) {
                expressions.add(criteriaBuilder.equal(root.get("sort"), sortId));
            }
            Integer author = articleListFormModel.getAuthor();
            if (author != null) {
                expressions.add(criteriaBuilder.equal(root.get("author"), author));
            }
            Boolean publish = articleListFormModel.getPublish();
            if (publish != null) {
                expressions.add(criteriaBuilder.equal(root.get("publish"), publish));
            }
            return predicate;
        }, pageable);
        Integer userId = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        modelAndView.setViewName("/admin/article_list");
        return modelAndView.addObject("articleListFormModel", articleListFormModel)
                .addObject("articleEntities", articleEntities)
                .addObject("sortEntities", sortService.getTreeListText())
                .addObject("userId", userId)
                .addObject("allArticleCount", articleRepository.count())
                .addObject("myArticleCount", articleRepository.countByAuthor(userId))
                .addObject("publishedArticleCount", articleRepository.countByPublishTrue())
                .addObject("unpublishedArticleCount", articleRepository.countByPublishFalse());
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
