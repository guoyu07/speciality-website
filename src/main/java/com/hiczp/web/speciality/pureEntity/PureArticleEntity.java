package com.hiczp.web.speciality.pureEntity;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.entity.SortEntity;

import java.sql.Timestamp;

/**
 * Created by czp on 17-5-23.
 */
public class PureArticleEntity {
    private int id;

    private String title;

    private String tag;

    private String image;

    private String content;

    private PureSortEntity sort;

    private PureUserEntity author;

    private Timestamp createTime;

    private int views;

    private boolean publish;

    public PureArticleEntity(ArticleEntity articleEntity) {
        id = articleEntity.getId();
        title = articleEntity.getTitle();
        tag = articleEntity.getTag();
        image = articleEntity.getImage();
        content = articleEntity.getContent();
        SortEntity sortEntity = articleEntity.getSortBySort();
        if (sortEntity != null) {
            sort = new PureSortEntity(sortEntity);
        }
        author = new PureUserEntity(articleEntity.getUserByAuthor());
        createTime = articleEntity.getCreateTime();
        views = articleEntity.getViews();
        publish = articleEntity.getPublish();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public PureSortEntity getSort() {
        return sort;
    }

    public void setSort(PureSortEntity sort) {
        this.sort = sort;
    }

    public PureUserEntity getAuthor() {
        return author;
    }

    public void setAuthor(PureUserEntity author) {
        this.author = author;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }
}
