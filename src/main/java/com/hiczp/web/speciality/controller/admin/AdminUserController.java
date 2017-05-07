package com.hiczp.web.speciality.controller.admin;

import com.hiczp.web.speciality.repository.LoginLogRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by czp on 17-3-11.
 */
@Controller
@RequestMapping("/admin")
public class AdminUserController {
    private LoginLogRepository loginLogRepository;

    public AdminUserController(LoginLogRepository loginLogRepository) {
        this.loginLogRepository = loginLogRepository;
    }

    @GetMapping("/login_log")
    public ModelAndView loginLog(ModelAndView modelAndView, Pageable pageable) {
        modelAndView.setViewName("/admin/login_log");
        return modelAndView.addObject("recentlyLoginLogs", loginLogRepository.findByOrderByTimeDesc(pageable))
                .addObject("path", "/admin/login_log");
    }
}
