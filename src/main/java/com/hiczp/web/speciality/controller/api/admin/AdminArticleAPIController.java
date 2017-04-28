package com.hiczp.web.speciality.controller.api.admin;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.exception.ArticleNotFoundException;
import com.hiczp.web.speciality.model.ArticleFormModel;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;

/**
 * Created by czp on 17-4-25.
 */
@RestController
@RequestMapping("/api/admin/article")
public class AdminArticleAPIController {
    private ArticleRepository articleRepository;
    private UserRepository userRepository;

    public AdminArticleAPIController(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity newArticle(@Valid ArticleFormModel articleFormModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle(articleFormModel.getTitle());
        articleEntity.setTag(articleFormModel.getTag());
        articleEntity.setImage(articleFormModel.getImage());
        articleEntity.setContent(articleFormModel.getContent());
        articleEntity.setSort(articleFormModel.getSort());
        articleEntity.setAuthor(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId());
        //如果未指定创建时间, 则使用当前时间
        Timestamp timestamp;
        if (articleFormModel.getCreateTime() == null) {
            timestamp = new Timestamp(System.currentTimeMillis());
        } else {
            timestamp = articleFormModel.getCreateTime();
        }
        articleEntity.setCreateTime(timestamp);
        articleEntity.setViews(0);
        articleEntity.setPublish(articleFormModel.getPublish());
        return new ResponseEntity<>(articleRepository.save(articleEntity).getId(), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity editArticle(@PathVariable Integer id, @Valid ArticleFormModel articleFormModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        ArticleEntity articleEntity = articleRepository.findOne(id);
        if (articleEntity == null) {
            throw new ArticleNotFoundException();
        }
        articleEntity.setTitle(articleFormModel.getTitle());
        articleEntity.setTag(articleFormModel.getTag());
        articleEntity.setImage(articleFormModel.getImage());
        articleEntity.setContent(articleFormModel.getContent());
        articleEntity.setSort(articleFormModel.getSort());
        articleEntity.setCreateTime(articleFormModel.getCreateTime());
        articleEntity.setPublish(articleFormModel.getPublish());
        articleRepository.save(articleEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
