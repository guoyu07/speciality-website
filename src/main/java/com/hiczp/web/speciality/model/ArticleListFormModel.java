package com.hiczp.web.speciality.model;

import org.springframework.stereotype.Component;

/**
 * Created by czp on 17-5-8.
 */
@Component
public class ArticleListFormModel {
    private String word;

    private Integer sortId;

    private Integer author;

    private Boolean publish;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }
}
