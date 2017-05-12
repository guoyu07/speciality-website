package com.hiczp.web.speciality.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by czp on 17-5-12.
 */
@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {
    @GetMapping
    public ModelAndView index(ModelAndView modelAndView) {
        return modelAndView;
    }
}
