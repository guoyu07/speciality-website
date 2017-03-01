package com.hiczp.web.speciality.controller.admin;

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
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("/admin/index");
        return modelAndView;
    }
}
