package com.hiczp.web.speciality.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by czp on 17-2-27.
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView, String error) {
        //已登录则跳转到后台首页
        if ((boolean) modelAndView.getModel().get("isLogined")) {
            modelAndView.setView(new RedirectView("/admin"));
            return modelAndView;
        }
        return modelAndView.addObject("error", error);
    }
}
