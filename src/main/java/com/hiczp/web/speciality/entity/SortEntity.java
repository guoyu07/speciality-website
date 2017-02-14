package com.hiczp.web.speciality.entity;

import javax.persistence.*;

/**
 * Created by czp on 17-2-13.
 */
@Entity
@Table(name = "sort", schema = "speciality_website", catalog = "")
public class SortEntity {
    private int id;
    private String name;
    private int parent;
    private int taxis;
    private String type;

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public void setTaxis(Integer taxis) {
        this.taxis = taxis;
    }

    @Id
    @Column(name = "id", nullable = false)
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
    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    @Basic
    @Column(name = "taxis", nullable = true)
    public int getTaxis() {
        return taxis;
    }

    public void setTaxis(int taxis) {
        this.taxis = taxis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SortEntity that = (SortEntity) o;

        if (id != that.id) return false;
        if (parent != that.parent) return false;
        if (taxis != that.taxis) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + parent;
        result = 31 * result + taxis;
        return result;
    }

    @Basic
    @Column(name = "type", nullable = true, length = 32)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
