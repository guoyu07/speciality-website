package com.hiczp.web.speciality.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by czp on 17-3-18.
 */
@Component
public class ArticleFormModel {
    private int id;

    @NotEmpty(message = "文章标题不能为空")
    private String title;

    private String tag;

    private String image;

    @NotEmpty(message = "文章内容不能为空")
    private String content;

    @NotNull(message = "分类不能为空")
    private Integer sort;

    private Timestamp createTime;

    @NotNull(message = "必须指定文章发布状态")
    private Boolean publish;

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }
}
