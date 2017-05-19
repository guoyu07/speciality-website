package com.hiczp.web.speciality.controller.api.admin;

import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.entity.UserEntity;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.LoginLogRepository;
import com.hiczp.web.speciality.repository.SpringSessionRepository;
import com.hiczp.web.speciality.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.SessionRepository;
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
    private SessionRepository sessionRepository;
    private SpringSessionRepository springSessionRepository;

    public AdminUserAPIController(UserRepository userRepository, ArticleRepository articleRepository, LoginLogRepository loginLogRepository, SessionRepository sessionRepository, SpringSessionRepository springSessionRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.loginLogRepository = loginLogRepository;
        this.sessionRepository = sessionRepository;
        this.springSessionRepository = springSessionRepository;
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
        //退出登录
        springSessionRepository.findByPrincipalName(userEntity.getEmail()).parallelStream().forEach(springSessionEntity -> sessionRepository.delete(springSessionEntity.getSessionId()));
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
            userEntity.setPassword(new BCryptPasswordEncoder().encode(password));
        }
        userRepository.save(userEntity);
        return new ResponseEntity(HttpStatus.OK);
    }
}
