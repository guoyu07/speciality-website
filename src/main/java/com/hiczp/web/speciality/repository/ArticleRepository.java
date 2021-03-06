package com.hiczp.web.speciality.repository;

import com.hiczp.web.speciality.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by czp on 17-2-17.
 */
public interface ArticleRepository extends PagingAndSortingRepository<ArticleEntity, Integer>, JpaSpecificationExecutor<ArticleEntity> {
    List<ArticleEntity> findByIdIn(Integer[] ids);

    List<ArticleEntity> findBySort(Integer sort);

    List<ArticleEntity> findTop5ByAndPublishTrueOrderByCreateTimeDesc();

    ArticleEntity findFirstBySortAndPublishTrueOrderByCreateTimeDesc(Integer sort);

    List<ArticleEntity> findTop3BySortAndPublishTrueOrderByCreateTimeDesc(Integer sort);

    List<ArticleEntity> findTop5BySortAndPublishTrueOrderByCreateTimeDesc(Integer sort);

    Page<ArticleEntity> findBySortAndPublishTrueOrderByCreateTimeDesc(Integer sort, Pageable pageable);

    Page<ArticleEntity> findByTitleContainsOrContentContainsOrTagContainsAndPublishTrueOrderByCreateTimeDesc(String title, String content, String tag, Pageable pageable);

    Long countByAuthor(Integer author);

    Long countByPublishTrue();

    Long countByPublishFalse();

    List<ArticleEntity> findByAuthor(Integer author);
}
