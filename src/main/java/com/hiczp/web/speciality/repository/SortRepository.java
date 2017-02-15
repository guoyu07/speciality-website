package com.hiczp.web.speciality.repository;

import com.hiczp.web.speciality.entity.SortEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by czp on 17-2-14.
 */
public interface SortRepository extends CrudRepository<SortEntity, Integer> {
    List<SortEntity> findByParentOrderByTaxis(Integer parent);

    List<SortEntity> findByParentAndNavbarOrderByTaxis(Integer parent, Byte navbar);
}
