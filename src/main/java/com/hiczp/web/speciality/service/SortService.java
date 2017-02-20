package com.hiczp.web.speciality.service;

import com.alibaba.fastjson.JSON;
import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.repository.ConfigRepository;
import com.hiczp.web.speciality.repository.SortRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czp on 17-2-14.
 */
@Service
public class SortService {
    private SortRepository sortRepository;
    private ConfigRepository configRepository;

    public SortService(SortRepository sortRepository, ConfigRepository configRepository) {
        this.sortRepository = sortRepository;
        this.configRepository = configRepository;
    }

    public List<SortEntity> getRootSorts() {
        return sortRepository.findByParentOrderByTaxis(0);
    }

    public List<SortEntity> getNavbarRootSorts() {
        List<SortEntity> sortEntities = new ArrayList<>();
        List<Integer> ids = JSON.parseArray(configRepository.findByKey("navbarSorts").getValue(), Integer.class);
        if (ids != null) {
            ids.forEach((id) ->
                    sortEntities.add(sortRepository.findOne(id))
            );
        }
        return sortEntities;
    }

    public List<SortEntity> getChildSorts(SortEntity sortEntity) {
        return sortRepository.findByParentOrderByTaxis(sortEntity.getId());
    }

    public SortEntity getParentSort(SortEntity sortEntity) {
        return sortRepository.findOne(sortEntity.getParent());
    }
}
