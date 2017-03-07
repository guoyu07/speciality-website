package com.hiczp.web.speciality.repository;

import com.hiczp.web.speciality.entity.LoginLogEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by czp on 17-3-5.
 */
public interface LoginLogRepository extends CrudRepository<LoginLogEntity, Integer> {
    List<LoginLogEntity> findTop5ByOrderByTimeDesc();
}