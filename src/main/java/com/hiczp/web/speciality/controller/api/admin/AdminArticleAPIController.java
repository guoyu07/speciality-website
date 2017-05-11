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
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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

    @PostMapping("/do")
    public ResponseEntity doAction(ArticleAction articleAction, Integer[] selectedArticle) {
        if (articleAction == null || selectedArticle == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Integer[] ids = Stream.of(selectedArticle).filter(Objects::nonNull).toArray(Integer[]::new);
        List<ArticleEntity> articleEntities = articleRepository.findByIdIn(ids);
        switch (articleAction) {
            case PUBLISH_ARTICLE: {
                articleEntities.parallelStream().forEach(articleEntity -> articleEntity.setPublish(true));
                articleRepository.save(articleEntities);
            }
            break;
            case CANCEL_PUBLISH: {
                articleEntities.parallelStream().forEach(articleEntity -> articleEntity.setPublish(false));
                articleRepository.save(articleEntities);
            }
            break;
            case DELETE_ARTICLE: {
                articleRepository.delete(articleEntities);
            }
            break;
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private enum ArticleAction {
        PUBLISH_ARTICLE,
        CANCEL_PUBLISH,
        DELETE_ARTICLE
    }
}
