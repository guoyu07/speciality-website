package com.hiczp.web.speciality.repository;

import com.hiczp.web.speciality.entity.SpringSessionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by czp on 17-5-18.
 */
public interface SpringSessionRepository extends CrudRepository<SpringSessionEntity, String> {
    List<SpringSessionEntity> findByPrincipalName(String principalName);
}
