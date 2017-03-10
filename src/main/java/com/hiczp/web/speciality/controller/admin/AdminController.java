package com.hiczp.web.speciality.controller.admin;

import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.LoginLogRepository;
import com.hiczp.web.speciality.repository.SortRepository;
import com.hiczp.web.speciality.repository.UserRepository;
import org.apache.catalina.util.ServerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by czp on 17-2-27.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private SortRepository sortRepository;
    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private LoginLogRepository loginLogRepository;

    public AdminController(SortRepository sortRepository, UserRepository userRepository, ArticleRepository articleRepository, LoginLogRepository loginLogRepository) {
        this.sortRepository = sortRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.loginLogRepository = loginLogRepository;
    }

    @GetMapping("")
    public ModelAndView index(ModelAndView modelAndView, @Value("${project.version}") String projectVersion) {
        modelAndView.setViewName("/admin/index");
        return modelAndView.addObject("activeSidebarItem", "overview")
                .addObject("projectVersion", projectVersion)
                .addObject("springBootVersion", SpringBootVersion.getVersion())
                .addObject("ServerInfo", ServerInfo.class)
                .addObject("System", System.class)
                .addObject("recentlyArticles", articleRepository.findTop5ByAndPublishTrueOrderByCreateTimeDesc())
                .addObject("sortCount", sortRepository.count())
                .addObject("articleCount", articleRepository.count())
                .addObject("userCount", userRepository.count())
                .addObject("recentlyLoginLogs", loginLogRepository.findTop5ByOrderByTimeDesc());
    }
}
