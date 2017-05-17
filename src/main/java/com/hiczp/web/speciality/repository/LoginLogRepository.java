package com.hiczp.web.speciality.repository;

import com.hiczp.web.speciality.entity.LoginLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by czp on 17-3-5.
 */
public interface LoginLogRepository extends PagingAndSortingRepository<LoginLogEntity, Integer> {
    List<LoginLogEntity> findTop5ByOrderByTimeDesc();

    Page<LoginLogEntity> findByOrderByTimeDesc(Pageable pageable);

    void deleteByUser(Integer user);
}
