package com.hiczp.web.speciality.controller.api;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.exception.SortNotFoundException;
import com.hiczp.web.speciality.pureEntity.PureSortEntity;
import com.hiczp.web.speciality.repository.SortRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czp on 17-5-23.
 */
@RestController
@RequestMapping("/api")
public class SortAPIController {
    private SortRepository sortRepository;

    public SortAPIController(SortRepository sortRepository) {
        this.sortRepository = sortRepository;
    }

    @GetMapping("/sorts")
    public ResponseEntity<List<PureSortEntity>> getAllSorts() {
        List<PureSortEntity> pureSortEntities = new ArrayList<>();
        sortRepository.findAll().forEach(sortEntity -> pureSortEntities.add(new PureSortEntity(sortEntity)));
        return new ResponseEntity<>(pureSortEntities, HttpStatus.OK);
    }

    @GetMapping("/sort/{id}")
    public ResponseEntity<PureSortEntity> getSort(@PathVariable Integer id) {
        SortEntity sortEntity = sortRepository.findOne(id);
        if (sortEntity == null) {
            throw new SortNotFoundException();
        }
        return new ResponseEntity<>(new PureSortEntity(sortEntity), HttpStatus.OK);
    }

    @GetMapping("/sort/{id}/articles")
    public ResponseEntity<int[]> getAllArticlesId(@PathVariable Integer id) {
        SortEntity sortEntity = sortRepository.findOne(id);
        if (sortEntity == null) {
            throw new SortNotFoundException();
        }
        int[] ids = sortEntity.getArticlesById().stream().filter(ArticleEntity::getPublish).mapToInt(ArticleEntity::getId).toArray();
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }
}
