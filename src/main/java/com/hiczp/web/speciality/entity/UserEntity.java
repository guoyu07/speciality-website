package com.hiczp.web.speciality.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by czp on 17-3-3.
 */
@Entity
@Table(name = "user", schema = "speciality_website", catalog = "")
public class UserEntity {
    private int id;
    private String password;
    private String email;
    private String nick;
    private Collection<ArticleEntity> articlesById;

    public UserEntity() {
    }

    public UserEntity(String email, String password, String nick) {
        this.email = email;
        this.password = password;
        this.nick = nick;
    }

    public UserEntity(int id, String email, String password, String nick) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nick = nick;
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
    @Column(name = "password", nullable = false, length = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "nick")
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "userByAuthor", fetch = FetchType.LAZY)
    public Collection<ArticleEntity> getArticlesById() {
        return articlesById;
    }

    public void setArticlesById(Collection<ArticleEntity> articlesById) {
        this.articlesById = articlesById;
    }
}
