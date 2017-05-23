package com.hiczp.web.speciality.pureEntity;

import com.hiczp.web.speciality.entity.UserEntity;

/**
 * Created by czp on 17-5-23.
 */
public class PureUserEntity {
    private int id;

    private String email;

    private String nick;

    private int articleCount;

    public PureUserEntity(UserEntity userEntity) {
        id = userEntity.getId();
        email = userEntity.getEmail();
        nick = userEntity.getNick();
        articleCount = userEntity.getArticlesById().size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }
}
