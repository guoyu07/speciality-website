package com.hiczp.web.speciality.repository;

import com.hiczp.web.speciality.entity.ConfigEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by czp on 17-2-14.
 */
public interface ConfigRepository extends CrudRepository<ConfigEntity, Integer> {
    ConfigEntity findByKey(String key);
}
