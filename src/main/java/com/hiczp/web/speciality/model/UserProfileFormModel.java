package com.hiczp.web.speciality.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
 * Created by czp on 17-5-12.
 */
@Component
public class UserProfileFormModel {
    private String email;

    private String password;

    @NotEmpty(message = "Nick 不能为空")
    private String nick;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
