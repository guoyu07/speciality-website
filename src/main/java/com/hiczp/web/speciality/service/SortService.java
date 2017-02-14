package com.hiczp.web.speciality.service;

import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.repository.SortRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by czp on 17-2-14.
 */
@Service
public class SortService {
    private SortRepository sortRepository;

    public SortService(SortRepository sortRepository) {
        this.sortRepository = sortRepository;
    }

    public List<SortEntity> getRootSorts() {
        return sortRepository.findByParentOrderByTaxis(0);
    }

    public List<SortEntity> getChildSorts(SortEntity sortEntity) {
        return sortRepository.findByParentOrderByTaxis(sortEntity.getId());
    }

    public SortEntity getParentSort(SortEntity sortEntity) {
        return sortRepository.findOne(sortEntity.getParent());
    }
}
