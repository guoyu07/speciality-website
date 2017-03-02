package com.hiczp.web.speciality.repository;

import com.hiczp.web.speciality.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by czp on 17-2-28.
 */
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findByEmail(String email);
}
