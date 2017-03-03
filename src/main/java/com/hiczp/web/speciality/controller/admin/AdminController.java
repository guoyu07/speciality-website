package com.hiczp.web.speciality.controller.admin;

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
    @GetMapping("")
    public ModelAndView index(ModelAndView modelAndView, @Value("${project.version}") String projectVersion, @Value("${maven.build.timestamp}") String mavenBuildTimestamp) {
        modelAndView.setViewName("/admin/index");
        return modelAndView.addObject("projectVersion", projectVersion)
                .addObject("mavenBuildTimestamp", mavenBuildTimestamp)
                .addObject("springBootVersion", SpringBootVersion.getVersion())
                .addObject("ServerInfo", ServerInfo.class)
                .addObject("System", System.class);
    }
}
