package com.hiczp.web.speciality.entity;

import com.hiczp.web.speciality.enumeration.SortType;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by czp on 17-3-3.
 */
@Entity
@Table(name = "sort", schema = "speciality_website", catalog = "")
public class SortEntity implements Cloneable {
    private int id;
    private String name;
    private Integer parent;
    private Integer taxis;
    private SortType type;
    private Collection<ArticleEntity> articlesById;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "parent", nullable = true)
    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    @Basic
    @Column(name = "taxis", nullable = true)
    public Integer getTaxis() {
        return taxis;
    }

    public void setTaxis(Integer taxis) {
        this.taxis = taxis;
    }

    @Basic
    @Column(name = "type", length = 32)
    @Enumerated(EnumType.STRING)
    public SortType getType() {
        return type;
    }

    public void setType(SortType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SortEntity that = (SortEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        if (taxis != null ? !taxis.equals(that.taxis) : that.taxis != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (taxis != null ? taxis.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public SortEntity clone() {
        try {
            return (SortEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @OneToMany(mappedBy = "sortBySort", fetch = FetchType.LAZY)
    public Collection<ArticleEntity> getArticlesById() {
        return articlesById;
    }

    public void setArticlesById(Collection<ArticleEntity> articlesById) {
        this.articlesById = articlesById;
    }
}
