package com.hiczp.web.speciality.pureEntity;

import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.enumeration.SortType;

/**
 * Created by czp on 17-5-23.
 */
public class PureSortEntity {
    private int id;

    private String name;

    private int parent;

    private SortType type;

    private Integer taxis;

    private int articleCount;

    public PureSortEntity(SortEntity sortEntity) {
        id = sortEntity.getId();
        name = sortEntity.getName();
        parent = sortEntity.getParent();
        type = sortEntity.getType();
        taxis = sortEntity.getTaxis();
        articleCount = sortEntity.getArticlesById().size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public SortType getType() {
        return type;
    }

    public void setType(SortType type) {
        this.type = type;
    }

    public Integer getTaxis() {
        return taxis;
    }

    public void setTaxis(Integer taxis) {
        this.taxis = taxis;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }
}
