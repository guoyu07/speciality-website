package com.hiczp.web.speciality.repository;

import com.hiczp.web.speciality.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by czp on 17-2-17.
 */
public interface ArticleRepository extends PagingAndSortingRepository<ArticleEntity, Integer> {
    List<ArticleEntity> findTop5ByOrderByCreateTimeDesc();

    ArticleEntity findFirstBySortOrderByCreateTimeDesc(Integer sort);

    List<ArticleEntity> findTop3BySortOrderByCreateTimeDesc(Integer sort);

    List<ArticleEntity> findTop5BySortOrderByCreateTimeDesc(Integer sort);

    Page<ArticleEntity> findBySortOrderByCreateTimeDesc(Integer sort, Pageable pageable);

    Page<ArticleEntity> findByTitleContainsOrContentContainsOrderByCreateTimeDesc(String title, String content, Pageable pageable);
}
