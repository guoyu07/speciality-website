package com.hiczp.web.speciality.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by czp on 17-2-8.
 */
@Controller
public class HomeController {
    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("/home/index");
    }
}
