package com.hiczp.web.speciality.controller.api.admin;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.entity.UserEntity;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.LoginLogRepository;
import com.hiczp.web.speciality.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by czp on 17-5-16.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminUserAPIController {
    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private LoginLogRepository loginLogRepository;
    private SessionRegistry sessionRegistry;

    public AdminUserAPIController(UserRepository userRepository, ArticleRepository articleRepository, LoginLogRepository loginLogRepository, SessionRegistry sessionRegistry) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.loginLogRepository = loginLogRepository;
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/validate_user")
    public boolean validateUser(String email) {
        return userRepository.findByEmail(email) == null;
    }

    @Transactional
    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        //ID 为 1 的用户不可删除
        if (id == 1) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        UserEntity userEntity = userRepository.findOne(id);
        if (userEntity == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        String email = userEntity.getEmail();
        //退出登录
        sessionRegistry.getAllPrincipals().parallelStream().forEach(object -> {
            User user = (User) object;
            if (user.getUsername().equals(email)) {
                sessionRegistry.getAllSessions(user, false).parallelStream().forEach(SessionInformation::expireNow);
            }
        });
        //将该用户的文章的作者改为 1
        List<ArticleEntity> articleEntities = articleRepository.findByAuthor(id);
        articleEntities.parallelStream().forEach(articleEntity -> articleEntity.setAuthor(1));
        articleRepository.save(articleEntities);
        //删除登录记录
        loginLogRepository.deleteByUser(id);
        //删除用户
        userRepository.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity editUser(@PathVariable Integer id, String email, String nick, String password) {
        UserEntity userEntity = userRepository.findOne(id);
        if (userEntity == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (email != null && !email.isEmpty()) {
            userEntity.setEmail(email);
        }
        if (nick != null && !nick.isEmpty()) {
            userEntity.setNick(nick);
        }
        if (password != null && !password.isEmpty()) {
            userEntity.setPassword(password);
        }
        userRepository.save(userEntity);
        return new ResponseEntity(HttpStatus.OK);
    }
}
