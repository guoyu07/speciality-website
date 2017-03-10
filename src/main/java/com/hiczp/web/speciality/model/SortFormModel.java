package com.hiczp.web.speciality.model;

import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.enumeration.SortType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
 * Created by czp on 17-3-9.
 */
@Component
public class SortFormModel {
    @NotEmpty(message = "分类名不能为空")
    private String name;

    private Integer parent;

    private SortType sortType;

    private Integer taxis;

    private Iterable<SortEntity> parentSorts;

    private SortType[] sortTypes = SortType.values();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public Integer getTaxis() {
        return taxis;
    }

    public void setTaxis(Integer taxis) {
        this.taxis = taxis;
    }

    public Iterable<SortEntity> getParentSorts() {
        return parentSorts;
    }

    public void setParentSorts(Iterable<SortEntity> parentSorts) {
        this.parentSorts = parentSorts;
    }

    public SortType[] getSortTypes() {
        return sortTypes;
    }

    public void setSortTypes(SortType[] sortTypes) {
        this.sortTypes = sortTypes;
    }
}
