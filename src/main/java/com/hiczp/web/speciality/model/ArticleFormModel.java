package com.hiczp.web.speciality.model;

import com.hiczp.web.speciality.entity.SortEntity;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by czp on 17-3-18.
 */
@Component
public class ArticleFormModel {
    @NotEmpty(message = "文章标题不能为空")
    private String title;

    private String image;

    private String content;

    private Integer sort;

    private Boolean publish;

    private List<SortEntity> sortEntities;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public List<SortEntity> getSortEntities() {
        return sortEntities;
    }

    public void setSortEntities(List<SortEntity> sortEntities) {
        this.sortEntities = sortEntities;
    }
}
