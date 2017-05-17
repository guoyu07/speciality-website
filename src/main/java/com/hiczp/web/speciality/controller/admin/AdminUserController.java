package com.hiczp.web.speciality.controller.admin;

import com.hiczp.web.speciality.entity.UserEntity;
import com.hiczp.web.speciality.repository.LoginLogRepository;
import com.hiczp.web.speciality.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by czp on 17-3-11.
 */
@Controller
@RequestMapping("/admin")
public class AdminUserController {
    private LoginLogRepository loginLogRepository;
    private UserRepository userRepository;

    public AdminUserController(LoginLogRepository loginLogRepository, UserRepository userRepository) {
        this.loginLogRepository = loginLogRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public ModelAndView userGet(ModelAndView modelAndView) {
        return modelAndView.addObject("userEntities", userRepository.findAll());
    }

    @PostMapping("/user")
    public ModelAndView userPost(ModelAndView modelAndView, String email, String nick, String password) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setEmail(email);
            userEntity.setNick(nick);
            userEntity.setPassword(password);
            userRepository.save(userEntity);
        }
        modelAndView.setView(new RedirectView("user"));
        return modelAndView;
    }

    @GetMapping("/login_log")
    public ModelAndView loginLog(ModelAndView modelAndView, Pageable pageable) {
        return modelAndView.addObject("recentlyLoginLogs", loginLogRepository.findByOrderByTimeDesc(pageable));
    }
}
