package com.hiczp.web.speciality.service;

import com.alibaba.fastjson.JSON;
import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.ConfigRepository;
import com.hiczp.web.speciality.repository.SortRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by czp on 17-2-14.
 */
@Service
public class SortService {
    private SortRepository sortRepository;
    private ConfigRepository configRepository;
    private ArticleRepository articleRepository;

    public SortService(SortRepository sortRepository, ConfigRepository configRepository, ArticleRepository articleRepository) {
        this.sortRepository = sortRepository;
        this.configRepository = configRepository;
        this.articleRepository = articleRepository;
    }

    public List<SortEntity> getNavbarRootSorts() {
        List<SortEntity> sortEntities = new ArrayList<>();
        List<Integer> ids = JSON.parseArray(configRepository.findByKey("navbarSorts").getValue(), Integer.class);
        if (ids != null) {
            ids.forEach(id -> sortEntities.add(sortRepository.findOne(id)));
        }
        return sortEntities;
    }

    public List<SortEntity> getChildSorts(Integer id) {
        return sortRepository.findByParentOrderByTaxis(id);
    }

    public List<SortEntity> getChildSorts(SortEntity sortEntity) {
        return getChildSorts(sortEntity.getId());
    }

    public SortEntity getParentSort(Integer id) {
        return getParentSort(sortRepository.findOne(id));
    }

    public SortEntity getParentSort(SortEntity sortEntity) {
        return sortRepository.findOne(sortEntity.getParent());
    }

    public List<SortEntity> getSiblingSorts(Integer id) {
        return getSiblingSorts(sortRepository.findOne(id));
    }

    public List<SortEntity> getSiblingSorts(SortEntity sortEntity) {
        return sortRepository.findByParentOrderByTaxis(sortEntity.getParent());
    }

    public List<SortEntity> getSidebarSorts(SortEntity sortEntity) {
        return getSiblingSorts(sortEntity);
    }

    public List<SortEntity> getSidebarSorts(ArticleEntity articleEntity) {
        return getSiblingSorts(articleEntity.getSort());
    }

    public List<SortEntity> getParentsChain(SortEntity sortEntity) {
        List<SortEntity> sortEntities = new ArrayList<>();
        SortEntity parentEntity = sortEntity;
        do {
            sortEntities.add(parentEntity);
            parentEntity = getParentSort(parentEntity);
        } while (parentEntity != null);

        Collections.reverse(sortEntities);
        return sortEntities;
    }
}
